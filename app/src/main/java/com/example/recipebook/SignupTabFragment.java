package com.example.recipebook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.recipebook.DAOs.UserDAO;
import com.example.recipebook.Entities.User;
import com.google.android.material.tabs.TabLayout;

public class SignupTabFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    EditText fullName;
    EditText eMail;
    EditText passWord;
    EditText confirmPassword;
    Button signup;
    public static UserDAO userDAO;
    float v=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);
        viewPager = getActivity().findViewById(R.id.view_pager); // Replace R.id.viewPager with the actual ID of your ViewPager in the layout

        userDAO = DatabaseConnect.getDBInstance(getContext()).getUserDAO();

        fullName = root.findViewById(R.id.fullname);
        eMail = root.findViewById(R.id.email);
        passWord = root.findViewById(R.id.password);
        confirmPassword = root.findViewById(R.id.confirm_password);
        signup = root.findViewById(R.id.signup_button);

        fullName.setTranslationX(800);
        eMail.setTranslationX(800);
        passWord.setTranslationX(800);
        confirmPassword.setTranslationX(800);
        signup.setTranslationX(800);

        fullName.setAlpha(v);
        eMail.setAlpha(v);
        passWord.setAlpha(v);
        confirmPassword.setAlpha(v);
        signup.setAlpha(v);

        fullName.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        eMail.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        passWord.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        confirmPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        signup.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();

//        return root;
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = fullName.getText().toString();
                String email = eMail.getText().toString();
                String password = passWord.getText().toString();
                String confirmpassword = confirmPassword.getText().toString();


                //Validations

                if (validateEmail(signup, email)) {
                    return;
                }

                if (validatePassword(signup, password, confirmpassword)) {
                    return;
                }

                if (validatePasswordRequirements(signup, password, confirmpassword)) {
                    return;
                }


                User newUser = new User(fullname, email, password);
                Context context = view.getContext();
                Toast.makeText(context, "User created", Toast.LENGTH_SHORT).show();

                userDAO.insertUser(newUser);
                if (newUser != null) {
//                    Toast.makeText(SignupActivity.this, newUser.toString(), Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(SignupActivity.this, "null", Toast.LENGTH_SHORT).show();
                }

//                // Switch to LoginTabFragment
//                viewPager.setCurrentItem(1); // Replace 1 with the index of your LoginTabFragment

                if (viewPager != null) {
                    viewPager.setCurrentItem(0); // Replace 1 with the index of your LoginTabFragment
                }

//                Intent intent = new Intent(getActivity(), HomeActivity.class); // Use getActivity() to get the current activity
//                startActivity(intent);
//                getActivity().finish();
            }
        });

        return root;
    }

    public static boolean validateEmail(View view, String email) {
        if (userDAO.getUserByEmail(email) != null) {
            Context context = view.getContext();
            Toast.makeText(context, "Email already used", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


    public static boolean validatePassword(View view, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            Context context = view.getContext();
            Toast.makeText(context, "Password and confirm password do not match", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public static boolean validatePasswordRequirements(View view, String password, String confirmPassword) {
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,16}$") ||
                !confirmPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,16}$")) {
            Context context = view.getContext();
            Toast.makeText(context, "Password and confirm password must be between 6 and 16 characters and contain at least one uppercase and lowercase letter and one digit", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}

