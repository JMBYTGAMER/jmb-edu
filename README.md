# JMB EDU

**Powered by Jobin Mathew Biju**

Native Android school-management app built with Kotlin, XML Views, and Material Design 3.

## Build an APK from a phone

This repository includes a GitHub Actions workflow. Every push to `main` starts a cloud build, and you can also run it manually.

1. Open the **Actions** tab in this repository.
2. Select **Build JMB EDU APK**.
3. Tap **Run workflow** and confirm.
4. Open the completed workflow run.
5. Under **Artifacts**, download **JMB-EDU-debug-APK**.
6. Extract the ZIP on your phone and install `app-debug.apk`.

## Current status

This is the first buildable UI foundation, not a finished school-management system. Student/teacher records, authentication, role enforcement, multi-school isolation, offline database, conflict-aware sync, backup, and reporting must be implemented and tested before real school data is used.

Do not use this starter build for real student information.
