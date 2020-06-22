package com.vanced.manager.core.base;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageInstaller;

import com.vanced.manager.core.installer.AppUninstallerService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DummyJava {

    public static void installApp(Activity activity, InputStream in, String pkg) throws IOException {
        PackageInstaller packageInstaller = activity.getPackageManager().getPackageInstaller();
        PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);
        params.setAppPackageName(pkg);
        int sessionId = packageInstaller.createSession(params);
        PackageInstaller.Session session = packageInstaller.openSession(sessionId);
        OutputStream outputStream = session.openWrite("install", 0, -1);
        byte[] buffer = new byte[65536];
        int c;
        while ((c = in.read(buffer)) != -1) {
            outputStream.write(buffer, 0, c);
        }
        session.fsync(outputStream);
        in.close();
        outputStream.close();
    }

}
