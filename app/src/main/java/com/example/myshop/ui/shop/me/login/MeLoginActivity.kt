package com.example.myshop.ui.shop.me.login

import android.content.Intent
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.example.myshop.R
import com.example.myshop.databinding.ActivityMeLoginBinding
import com.example.myshop.utils.ToastUtils
import com.example.myshop.viewmodel.shop.me.login.MeLoginViewModel
import com.shop.base.BaseActivity
import com.shop.utils.SpUtils
import kotlinx.android.synthetic.main.activity_me_login.*

class MeLoginActivity (var lid: Int = R.layout.activity_me_login): BaseActivity
<MeLoginViewModel, ActivityMeLoginBinding>(lid, MeLoginViewModel::class.java),
    View.OnClickListener{
    override fun initView() {
        initClick()
        initResult()
    }

    //TODO 设置输入密码的眼睛
    private fun initResult() {
        //眼睛
        iv_img_pw!!.tag = 1
        et_input_pw!!.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    //TODO 点击事件
    fun initClick(){
        mDataBinding!!.ivImgPw.setOnClickListener(this)
        mDataBinding!!.btnLogin.setOnClickListener(this)
        mDataBinding!!.tvLoginRegister.setOnClickListener(this)
    }

    //TODO 获取数据
    override fun initVM() {
        mViewModel.melogin.observe(this, Observer {
            if (it != null && it.size > 0) {
                var token = it.get(0).token
                if (!TextUtils.isEmpty(token)) {
                    SpUtils.instance!!.setValue("token", token)
                    SpUtils.instance!!.setValue("username",  it.get(0).userInfo.username)
                    SpUtils.instance!!.setValue("nickname", it.get(0).userInfo.nickname)
                    SpUtils.instance!!.setValue("uid", it.get(0).userInfo.uid)
                    SpUtils.instance!!.setValue("avatar", it.get(0).userInfo.avatar)
                    SpUtils.instance!!.setValue("mark", "")
                    Log.e("TAG", "initVM: "+token )
                    ToastUtils.s(this, getString(R.string.tips_login_ok))
                    finish()
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_img_pw -> {       //眼睛
                initPwImg()
            }
            R.id.btn_login -> {        //登录
                initLogin()
            }
            R.id.tv_login_register -> {        //注册
                initRegist()
            }
        }
    }

    //TODO 判断登录
    private fun initLogin() {
        var username = et_input_username!!.text.toString()
        var password = et_input_pw!!.text.toString()
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            var token = SpUtils.instance!!.getString("token")
            if (token != null) {
                mViewModel.MeLogin(username,password)
                if(token!=null){
                    mViewModel.MeLogin(username,password)
                    SpUtils.instance!!.setValue("username", username)
                } else {
                    ToastUtils.s(this, getString(R.string.tips_login))
                }
            }else {
                ToastUtils.s(this, getString(R.string.tips_login))
            }
        } else {
            ToastUtils.s(this, getString(R.string.tips_login_input))
        }
    }

    //TODO 输入密码的小眼睛
    private fun initPwImg() {
        //点击的第几次
        val tag = iv_img_pw!!.tag as Int
        //第一次显示
        if (tag == 1) {
            iv_img_pw!!.setImageResource(R.mipmap.ic_pw_close)
            iv_img_pw!!.tag = 2//关闭
            et_input_pw!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {  //第一次显示
            iv_img_pw!!.setImageResource(R.mipmap.ic_pw_open)
            iv_img_pw!!.tag = 1
            et_input_pw!!.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }

    //TODO 注册
    private fun initRegist(){
        val intent = Intent(this, MeRegisterActivity::class.java)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 100 && requestCode == 100) {

            var regist_username = data!!.getStringExtra("username").toString()
            var regist_password = data!!.getStringExtra("password").toString()

            //赋值
            if (!TextUtils.isEmpty(regist_username) && !TextUtils.isEmpty(regist_password)) {
                et_input_username.setText(regist_username)
                et_input_pw.setText(regist_password)
            } else {
                Log.e("TAG", "initResult: " + "注册回传值为空")
            }

        }
    }

    override fun initData() {

    }

    override fun initVariable() {

    }

}