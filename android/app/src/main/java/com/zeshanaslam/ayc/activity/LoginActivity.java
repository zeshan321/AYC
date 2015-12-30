package com.zeshanaslam.ayc.activity;

import android.app.ProgressDialog;
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


import com.zeshanaslam.ayc.MainActivity;
import com.zeshanaslam.ayc.R;
import com.zeshanaslam.ayc.database.UserDB;
import com.zeshanaslam.ayc.utils.CallBack;
import com.zeshanaslam.ayc.utils.HTTPSManager;
import com.zeshanaslam.ayc.utils.LoginHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.Bind;

public class LoginActivity extends AppCompatActivity {

    // Edit text views
    @Bind(R.id.login_username)
    EditText _usernameText;
    @Bind(R.id.login_password)
    EditText _passwordText;

    // Text views
    @Bind(R.id.tv_signup)
    TextView _signupUser;

    // Button views
    @Bind(R.id.login_button)
    Button _loginButton;

    // Strings
    @BindString(R.string.server_url)
    String _serverURL;
    @BindString(R.string.invalid_username)
    String _invalidUsername;
    @BindString(R.string.invalid_password)
    String _invalidPassword;
    @BindString(R.string.incorrect_login)
    String _invalidLogin;
    @BindString(R.string.login_error)
    String _loginError;

    // Dialogs
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Bind views
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent_next = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent_next);
                finish();
            }
        });
    }

    private void login() {
        if (!validateInput()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String username = _usernameText.getText().toString();
        final String password = _passwordText.getText().toString();
        final UserDB userDB = new UserDB(this);

        HTTPSManager httpsManager = new HTTPSManager();
        httpsManager.runConnection(_serverURL + "/login?user=" + username + "&pass=" + password, new CallBack() {

            @Override
            public void onRequestComplete(String response) {
                LoginHandler loginHandler = new LoginHandler(response);

                if (loginHandler.loginCheck()) {
                    userDB.addUser(username, password, loginHandler.getVideos());

                    onLoginSuccess();
                } else {
                    onLoginFailed();
                }
            }

            @Override
            public void onRequestFailed() {
                onLoginError();
            }
        });
    }

    private void onLoginSuccess() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressDialog.hide();
                _loginButton.setEnabled(true);

                Intent intent_next = new Intent(LoginActivity.this, MainActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                startActivity(intent_next);
                finish();
            }
        });
    }

    private void onLoginFailed() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressDialog.hide();
                Toast.makeText(getBaseContext(), _invalidLogin, Toast.LENGTH_LONG).show();

                _loginButton.setEnabled(true);
            }
        });
    }

    private void onLoginError() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressDialog.hide();
                Toast.makeText(getBaseContext(), _loginError, Toast.LENGTH_LONG).show();

                _loginButton.setEnabled(true);
            }
        });
    }

    private boolean validateInput() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty()) {
            _usernameText.setError(_invalidUsername);
            valid = false;
        } else {
            _usernameText.setError(null);
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
