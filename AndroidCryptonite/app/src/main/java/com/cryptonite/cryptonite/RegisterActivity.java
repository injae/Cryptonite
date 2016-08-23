package com.cryptonite.cryptonite;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import Crypto.SHAEncrypt;
import Function.C_Toast;
import Function.Client_Server_Connector;
import Function.PacketRule;

/**
 * Created by olleh on 2016-07-22.
 */
public class RegisterActivity extends AppCompatActivity implements PacketRule{

    private UserRegisterTask mRegisterTask = null;


    private View mRegisterFormView;
    private View mProgressView;
    private EditText mname,mid,mpassword,mpassword_re,memail;
    private ImageButton mRegisterButton;
    private Boolean sameId = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        mname = (EditText) findViewById(R.id.register_name);
        mid = (EditText) findViewById(R.id.register_id);
        mpassword = (EditText) findViewById(R.id.register_password);
        mpassword_re = (EditText) findViewById(R.id.register_password_retype);
        memail = (EditText) findViewById(R.id.register_email);

        mRegisterButton = (ImageButton) findViewById(R.id.register_attempt_button);

        // OnClick Register Button
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        mid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sameId = false;
                mid.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        memail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                attemptRegister();
                return true;
            }
        });
    }

    private void attemptRegister() {

        if (mRegisterTask!=null) {
            return;
        }

        //reset errors
        mname.setError(null);
        mid.setError(null);
        mpassword.setError(null);
        mpassword_re.setError(null);
        memail.setError(null);

        String name = mname.getText().toString();
        String id = mid.getText().toString();
        String password = mpassword.getText().toString();
        String password_re = mpassword_re.getText().toString();
        String email = memail.getText().toString();

        View focusView = null;
        boolean cancel=false;


        //set error
        if(TextUtils.isEmpty(email)) {
            memail.setError(getString(R.string.error_field_required));
            focusView = memail;
            cancel = true;
        } else if (!isValidEmail(email)){
            memail.setError(getString(R.string.error_invalid_email));
            focusView = memail;
            cancel = true;
        }
        if(password.length() < 5) {
            mpassword.setError(getString(R.string.error_invalid_password));
            focusView = mpassword;
            cancel = true;
        }
        else if (!password.equals(password_re)) {
            mpassword_re.setError(getString(R.string.error_notmatch_password));
            focusView = mpassword_re;
            cancel = true;
        }
        if(TextUtils.isEmpty(password)) {
            mpassword.setError(getString(R.string.error_field_required));
            focusView = mpassword;
            cancel = true;
        }
        if(TextUtils.isEmpty(id)) {
            mid.setError(getString(R.string.error_field_required));
            focusView = mid;
            cancel = true;
        }
        if(TextUtils.isEmpty(name)) {
            mname.setError(getString(R.string.error_field_required));
            focusView = mname;
            cancel = true;
        }





        //process register
        if (!cancel) {
            showProgress(true);
            // name, id ,password, email
            mRegisterTask = new UserRegisterTask(mname.getText().toString(), mid.getText().toString(), mpassword.getText().toString(), memail.getText().toString());
            mRegisterTask.execute((Void) null);
        } else {
            focusView.requestFocus();
        }
    }


    private boolean isValidEmail(String email){
        if (email.contains("@") && email.contains("."))
            return true;
        else return false;
    }

    private void setErrorId(){
        mid.setError(getString(R.string.error_used_id));
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

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Process Registeration
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mname;
        private final String mid;
        private final String mPassword;
        private final String memail;


        UserRegisterTask(String name, String id, String password, String email) {

            mname = name;
            mid = id;
            mPassword = password;
            memail = email;

        }

        //Register...
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {

                Client_Server_Connector css = Client_Server_Connector.getInstance();

                byte[] op = new byte[1024];
                byte size;

                //check duplicated id
                if (!sameId)
                {
                    size = 2;
                    op[0] = SIGN_UP;
                    op[1] = DUPLICATION_CHECK_FUNCTION; // id check mode
                    op[2] = size;

                    css.send.setPacket(op).write();
                    css.send.setPacket(mid.getBytes(),500).write();


                    byte[] receiveData;
                    css.receive.setAllocate(2);
                    receiveData = css.receive.read().getByte();

                    switch (receiveData[0])
                    {
                        case EMPTY_ID:
                            sameId = true;
                            break;
                        case EXIST_ID:
                            sameId = false;
                            break;
                        default:
                            sameId = false;
                            break;
                    }
                }


                //no duplicated Id > registeration
                if (sameId) {
                    size = 5;
                    op[1] = SIGN_UP_FUNCTION; // registration mode
                    op[2] = size;

                    Charset cs = Charset.forName("UTF-8");
                    ByteBuffer name = cs.encode(mname);
                    name.flip();


                    css.send.setPacket(op).write();
                    css.send.setPacket(name.array(),500).write();
                    css.send.setPacket(mid.getBytes(),500).write();
                    css.send.setPacket(SHAEncrypt.SHAEncrypt(mPassword).getBytes(),500).write();
                    css.send.setPacket(memail.getBytes(),500).write();

                    css.receive.setAllocate(1);
                    byte[] result = css.receive.read().getByte();

                    if(result[0] == 0)
                    {
                        return false;
                    }

                } else {
                    setErrorId();
                    return true;
                }
            } catch (IOException e){
                return false;
            }
            return true;
        }

        //finish register
        @Override
        protected void onPostExecute(final Boolean success) {
            mRegisterTask = null;
            showProgress(false);

            C_Toast toast = new C_Toast(getApplicationContext());

            if (!sameId)
                return;
            if (success) {
                toast.showToast("Sign Up Success!",Toast.LENGTH_LONG);
                finish();
            } else
            {
                toast.showToast("Sign Up Failed...",Toast.LENGTH_LONG);
            }
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            showProgress(false);
        }
    }


}
