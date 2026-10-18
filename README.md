# JMB EDU

**Powered by Jobin Mathew Biju**

Native Android school-management app built with Kotlin, XML Views, and Material Design 3.

## Build an APK using your phone
GitHub Actions builds the debug APK in the cloud.

1. Open the **Actions** tab in this repository.
2. Select **Build JMB EDU APK**.
3. Open the latest successful run.
4. Under **Artifacts**, download **JMB-EDU-debug-APK**.
5. Extract the ZIP and install `app-debug.apk` on an Android test device.

The Releases page may also have the latest debug APK:
https://github.com/JMBYTGAMER/jmb-edu/releases

## What works in this development build
- Generated JMB EDU launcher icon and role-preview sign-in screen.
- Demo dashboard for Student, Teacher, Senior Teacher, Headmaster, School Admin, and Owner roles.
- Student demo directory with add, view, and delete sample records using Room, DAO, repository, and ViewModel.
- Daily demo attendance: mark sample students Present or Absent, review totals, and save records by date in the local Room database.
- Sample student and attendance records persist on the device.

## Important limitations
The role picker is only a **demo preview**. It is not authentication and does not grant real permissions. The sign-in button will not authenticate a real account until a dedicated backend is configured. Demo records are stored locally on the device and do not sync to a school or cloud.

Real authentication, server-enforced role-based access control, multi-school data isolation, encrypted/offline sync conflict handling, backups, exams, reporting, and production QA still need to be implemented. Do not enter real student information or use this build as a production school system.
