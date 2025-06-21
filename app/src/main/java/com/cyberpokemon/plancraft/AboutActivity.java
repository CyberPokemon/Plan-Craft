package com.cyberpokemon.plancraft;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.aboutpage), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        String aboutDescription = "PlanCraft is your ultimate daily planning and task management companion, designed to help you stay focused, productive, and organized every single day.\n\n"
                + "Whether you're a student, professional, entrepreneur, or simply someone looking to build better habits, PlanCraft brings structure and clarity to your life with an intuitive, distraction-free interface.\n\n"
                + "✨ Key Features\n"
                + "✅ To-Do List Management\n"
                + "Quickly add, edit, or delete tasks. Mark tasks as completed and track your progress with ease.\n\n"
                + "📅 Daily Planner\n"
                + "Organize your day by scheduling tasks, appointments, and reminders—all in one place.\n\n"
                + "🕒 Task Reminders\n"
                + "Get timely notifications so you never miss a deadline or forget a task again.\n\n"
                + "🎯 Minimalist & User-Friendly Design\n"
                + "Built with simplicity and speed in mind—navigate effortlessly without clutter.\n\n"
                + "🔒 Offline & Secure\n"
                + "Your data stays on your device—PlanCraft works offline with no sign-in required.";


        TextView aboutdescriptiontextview = findViewById(R.id.aboutdescriptiontextview);

        aboutdescriptiontextview.setText(aboutDescription);

        TextView githubLink = findViewById(R.id.github_link);
        githubLink.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/cyberpokemon/PlanCraft"));
            startActivity(browserIntent);
        });
    }
}