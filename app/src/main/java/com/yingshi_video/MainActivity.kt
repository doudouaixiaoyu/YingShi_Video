package com.yingshi_video

import android.content.Context
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.SurfaceHolder
import android.widget.Toast
import com.videogo.errorlayer.ErrorInfo
import com.videogo.openapi.EZConstants
import com.videogo.openapi.EZOpenSDK
import com.videogo.openapi.EZPlayer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback {

    private var yinShiLoginDynamicReceiver: YinShiLoginDynamicReceiver? = null
    private var mHandler: MHandler? = null
    private var player: EZPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerDynamicReceiver()
        initEZOpenSDK()

        btn_Login.setOnClickListener {
            getAccessToken()
        }

        btn_Play.setOnClickListener {
            startRealPlay()
        }
    }

    /**
     * 初始化EZOpenSDK
     */
    private fun initEZOpenSDK() {
        EZOpenSDK.showSDKLog(true)
        EZOpenSDK.enableP2P(false)
        EZOpenSDK.initLib(this.application, "26810f3acd794862b608b6cfbc32a6b8")
        val toast = Toast.makeText(applicationContext, "初始化EZOpenSDK成功", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    /**
     * 得到AccessToken
     */
    private fun getAccessToken() {
        //EZOpenSDK.getInstance().openLoginPage()
        EZOpenSDK.getInstance()
            .setAccessToken("ra.10godwdv2klgjnwmcr5nk8z979stt55s-2300r5xlbq-0y8kack-ud92tknir")
        val toast = Toast.makeText(applicationContext, "得到AccessToken成功", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    private fun startRealPlay() {
        player = EZOpenSDK.getInstance().createPlayer("203751922", 1)
        mHandler = MHandler(applicationContext)
        player?.setHandler(mHandler)
        player?.setSurfaceHold(sur_video.holder)
        sur_video.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                val toast = Toast.makeText(applicationContext, "现在正在创建holder", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP, 0, 0)
                toast.show()
                player?.setSurfaceHold(sur_video.holder)
            }

            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                player?.setSurfaceHold(null)
            }
        })
        player?.startRealPlay()
    }

    /**
     * 注册动态广播
     */
    private fun registerDynamicReceiver() {
        val interFilter = IntentFilter()
        interFilter.addAction("com.videogo.action.OAUTH_SUCCESS_ACTION")
        yinShiLoginDynamicReceiver = YinShiLoginDynamicReceiver()
        registerReceiver(yinShiLoginDynamicReceiver, interFilter)
    }

    override fun onStart() {
        super.onStart()
        Log.i("Tag", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("Tag", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("Tag", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("Tag", "onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("Tag", "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("Tag", "onDestroy")
        val toast = Toast.makeText(applicationContext, "开始onDestroy", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
        // 销毁广播资源
        unregisterReceiver(yinShiLoginDynamicReceiver)
        // 销毁AccessToken
        EZOpenSDK.getInstance().logout()
        // 释放资源
        player?.release()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class MHandler(private val context: Context) : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_SUCCESS -> {
                    val toast = Toast.makeText(context, "播放成功", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.TOP, 0, 0)
                    toast.show()
                    return
                }
                EZConstants.EZRealPlayConstants.MSG_REALPLAY_PLAY_FAIL -> {
                    // 播放失败，得到错误信息
                    val errorInfo: ErrorInfo = msg.obj as ErrorInfo
                    // 得到播放失败错误码
                    val code = errorInfo.errorCode
                    // 得到播放失败模块错误码
                    val codeStr = errorInfo.moduleCode
                    // 得到播放失败描述
                    val description = errorInfo.description
                    // 得到播放失败解决方案
                    val sulution = errorInfo.sulution
                    val toast = Toast.makeText(context, "播放失败,错误码$code", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.TOP, 0, 0)
                    toast.show()
                    return
                }
            }
        }
    }
}
