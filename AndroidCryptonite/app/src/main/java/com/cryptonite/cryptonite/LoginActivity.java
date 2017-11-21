package com.cryptonite.cryptonite;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.scottyab.rootbeer.RootBeer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import Crypto.KeyReposit;
import Crypto.SHAEncrypt;
import Function.C_Toast;
import Function.Client_Info;
import Function.Client_KeyExchange;
import Function.Client_Server_Connector;
import Function.PacketRule;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via id/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, PacketRule {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    // UI references.
    private AutoCompleteTextView midView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        midView = (AutoCompleteTextView) findViewById(R.id.id);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    attemptLogin();
                    return true;
            }
        });

        ImageButton mRegisterButton = (ImageButton) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });

        ImageButton midSignInButton = (ImageButton) findViewById(R.id.sign_in_button);
        midSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        RootBeer rootBeer = new RootBeer(this);
        if (rootBeer.isRooted())
        {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("루팅 된 기기는 이용할 수 없습니다.");
            dlgAlert.setTitle("Root detected!!");
            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Runtime.getRuntime().exit(0);
                            //dismiss the dialog
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_MENU)
        {
            // get prompts.xml view
            LayoutInflater layoutInflater = LayoutInflater.from(LoginActivity.this);
            View promptView = layoutInflater.inflate(R.layout.test_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
            alertDialogBuilder.setView(promptView);

            final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Client_Info.setIp(editText.getText().toString());
                            new C_Toast(LoginActivity.this).showToast(editText.getText().toString(), Toast.LENGTH_LONG);
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            // create an alert dialog
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();

        }


        return super.onKeyDown(keyCode, event);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(midView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid id, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        midView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String id = midView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if(!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        // Check for a valid id address.
        if (!isidValid(id)) {
            midView.setError(getString(R.string.error_invalid_id));
            focusView = midView;
            cancel = true;
        } else if (TextUtils.isEmpty(id)) {
            midView.setError(getString(R.string.error_field_required));
            focusView = midView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(id, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isidValid(String id) {
        //TODO: Replace this with your own logic
        return id.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only id addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Identity
                .CONTENT_ITEM_TYPE},

                // Show primary id addresses first. Note that there won't be
                // a primary id address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> ids = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ids.add(cursor.getString(ProfileQuery.IDENTIFY));
            cursor.moveToNext();
        }

        addidsToAutoComplete(ids);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addidsToAutoComplete(List<String> idAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, idAddressCollection);

        midView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Identity.IDENTITY,
                ContactsContract.CommonDataKinds.Identity.IS_PRIMARY,
        };

        int IDENTIFY = 0;
        int IS_PRIMARY = 1;
    }

    private void setInvalidId(){
        midView.setError(getString(R.string.error_invalid_id));
        midView.requestFocus();
    }

    private void setIncorrectPwd(){
        mPasswordView.setError(getString(R.string.error_incorrect_password));
        mPasswordView.requestFocus();
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Byte, Boolean> {

        private final String mid;
        private final String mPassword;
        private byte[] receiveData;

        UserLoginTask(String id, String password) {
            mid = id;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                new Client_KeyExchange();
                Client_Server_Connector css = Client_Server_Connector.getInstance();

                byte[] op = new byte[1024];
                op[0] = LOGIN;
                op[1] = 3;

                css.send.setPacket(op).write();
                css.send.setPacket(mid.getBytes(),500).write();
                css.send.setPacket(SHAEncrypt.SHAEncrypt(mPassword).getBytes(),500).write();


                receiveData = css.receive.setAllocate(1024).read().getByte();



                switch(receiveData[0])
                {
                    case 1:
                        publishProgress(receiveData[0]);
                        return false;
                    case 2:

                        Charset cs = Charset.forName("UTF-8");


                        ArrayList<String> gpcode = new ArrayList<String>();
                        ArrayList<String> gpname = new ArrayList<String>();

                        byte[] gpcount = css.receive.setAllocate(100).read().getByte();
                        String name = cs.decode(css.receive.setAllocate(500).read().getByteBuf()).toString().trim();
                       // String uscode = css.receive.setAllocate(100).read().getByte().toString().trim();
                        byte[] aeskey = css.receive.setAllocate(32).read().getByte();

                        (KeyReposit.getInstance()).set_aesKey(new SecretKeySpec(aeskey, "AES"));

                        for(int i=0;i<gpcount[0];i++)
                        {
                            gpcode.add(new String(css.receive.setAllocate(100).read().getByte()).trim());
                            gpname.add(new String(css.receive.setAllocate(500).read().getByte()).trim());
                        }

                        Client_Info.getInstance().init(mid,name,aeskey,gpcode,gpname);

                        return true;
                    case 3:
                        publishProgress(receiveData[0]);
                        return false;
                    default:
                        return false;
                }
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(Byte... values) {
            switch(values[0])
            {
                case 1:
                    setInvalidId();
                    break;
                case 3:
                    setIncorrectPwd();
                    break;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

