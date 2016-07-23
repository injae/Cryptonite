package com.cryptonite.cryptonite;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by olleh on 2016-07-22.
 */
public class RegisterActivity extends AppCompatActivity {

    private UserRegisterTask mRegisterTask = null;


    private View mRegisterFormView;
    private View mProgressView;
    private EditText mname,mid,mpassword,mpassword_re,memail;
    private Button mRegisterButton;

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

        mRegisterButton = (Button) findViewById(R.id.register_attempt_button);

        // OnClick Register Button
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
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
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }


            return true;
        }

        //finish register
        @Override
        protected void onPostExecute(final Boolean success) {
            mRegisterTask = null;
            showProgress(false);

            if (success) {
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
            showProgress(false);
        }
    }


}
