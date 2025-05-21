# 📱 XSU Detector – Root Detection Android App

**XSU Detector** is a simple Android app that detects whether a device is **rooted** and informs the user. It also sends a **boot-time notification** so users can quickly check root status after restarting their device.

---

## 🔧 Features

- ✅ Detects if the device is rooted  
- 🔒 Shows warning and exits if rooted  
- ✅ Confirms safety if device is not rooted  
- 🔔 Displays a notification after device boots  
- 🔐 Requests notification permission on Android 13+ (API 33+)

---

## 🚀 How It Works

### 1. **Root Detection (`MainActivity.java`)**

When **XSU Detector** launches:

- It performs 3 checks:
  - Looks for the `su` binary in common system directories.
  - Attempts to execute the `su` command.
  - Searches for known root apps (e.g. Magisk, SuperSU).

**Decision:**

- 📛 **If rooted** → shows a warning and closes the app.  
- ✅ **If not rooted** → shows a safe-to-use confirmation dialog.

---

### 2. **Request Notification Permission**

For Android 13+ (`API 33` and above), **XSU Detector** requests the `POST_NOTIFICATIONS` permission at runtime, which is needed to show notifications.

---

### 3. **Boot Broadcast Receiver (`BootReceiver.java`)**

After a device restarts:

- **XSU Detector** listens for the `BOOT_COMPLETED` broadcast.
- When received:
  - It shows a notification: **“Tap to check root status.”**
  - Tapping the notification launches the app.

---

## 📄 License
This project is licensed under the MIT License – see the LICENSE file for details.

## 🙌 Contributing
Contributions, issues, and feature requests are welcome!
Feel free to open a pull request or submit an issue to help improve XSU Detector.
