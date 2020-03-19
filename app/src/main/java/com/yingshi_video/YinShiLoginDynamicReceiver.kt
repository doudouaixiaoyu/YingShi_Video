package com.yingshi_video

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.widget.Toast

/**
 * Author: GeHaoRan
 * Date: 2020-03-14 22:39
 * Doc: 萤石登录成功后的动态广播
 */
class YinShiLoginDynamicReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("TAG", "登录成功")
        val toast = Toast.makeText(context, "登录成功提示,已获得AccessToken", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }
}