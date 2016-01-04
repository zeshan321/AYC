package com.zeshanaslam.ayc.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zeshanaslam.ayc.R;
import com.zeshanaslam.ayc.database.UserDB;
import com.zeshanaslam.ayc.utils.HTTPSCallBack;
import com.zeshanaslam.ayc.utils.HTTPSManager;
import com.zeshanaslam.ayc.utils.LoginHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {

    // Edit text views
    @Bind(R.id.signup_username)
    EditText _usernameText;
    @Bind(R.id.signup_email)
    EditText _emailText;
    @Bind(R.id.signup_password)
    EditText _passwordText;

    // Text views
    @Bind(R.id.tv_login)
    TextView _signupUser;

    // Button views
    @Bind(R.id.signup_button)
    Button _signupButton;

    // Strings
    @BindString(R.string.server_url)
    String _serverURL;
    @BindString(R.string.invalid_username)
    String _invalidUsername;
    @BindString(R.string.invalid_email)
    String _invalidEmail;
    @BindString(R.string.invalid_password)
    String _invalidPassword;
    @BindString(R.string.already_registered)
    String _alreadyRegistered;
    @BindString(R.string.signup_error)
    String _signupError;
    @BindString(R.string.registering)
    String _dialogReg;
    @BindString(R.string.auth)
    String _dialogAuth;
    @BindString(R.string.load_data)
    String _dialogData;

    private ProgressDialog progressDialog;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Set context
        this.context = this;

        // UI
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Bind views
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signup();
            }
        });


        _signupUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent_next = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent_next);
                finish();
            }
        });
    }

    private void signup() {
        progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);

        if (!validateInput()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(_dialogReg);
        progressDialog.show();

        final String username = _usernameText.getText().toString();
        final String password = _passwordText.getText().toString();
        String email = _emailText.getText().toString();

        HTTPSManager httpsManager = new HTTPSManager();
        httpsManager.runConnection(_serverURL + "/register?user=" + username + "&pass=" + password + "&email=" + email, new HTTPSCallBack() {

            @Override
            public void onRequestComplete(String response) {

                if (signupCheck(response)) {
                    onSignupSuccess(username, password);
                } else {
                    onSignupFailed();
                }
            }

            @Override
            public void onRequestFailed() {
                onSignupError();
            }
        });
    }

    private boolean signupCheck(String JSON) {
        boolean login = true;

        try {
            JSONObject jsonObject = new JSONObject(JSON);

            login = jsonObject.getBoolean("succeed");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return login;
    }

    private void onSignupSuccess(final String username, final String password) {
        final UserDB userDB = new UserDB(this);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(_dialogAuth);
            }
        });

        HTTPSManager httpsManager = new HTTPSManager();
        httpsManager.runConnection(_serverURL + "/login?user=" + username + "&pass=" + password, new HTTPSCallBack() {

            @Override
            public void onRequestComplete(String response) {
                final LoginHandler loginHandler = new LoginHandler(response, context);

                userDB.addUser(username, password, loginHandler.getVideos());

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setMessage(_dialogData);
                    }
                });

                loginHandler.loadYears(_serverURL);
            }

            @Override
            public void onRequestFailed() {
                onSignupError();
            }
        });
    }

    private void onSignupFailed() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressDialog.hide();
                Toast.makeText(getBaseContext(), _alreadyRegistered, Toast.LENGTH_LONG).show();

                _signupButton.setEnabled(true);
            }
        });
    }

    private void onSignupError() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressDialog.hide();
                Toast.makeText(getBaseContext(), _signupError, Toast.LENGTH_LONG).show();

                _signupButton.setEnabled(true);
            }
        });
    }

    private boolean validateInput() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty()) {
            _usernameText.setError(_invalidUsername);
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(_invalidEmail);
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 19) {
            _passwordText.setError(_invalidPassword);
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
