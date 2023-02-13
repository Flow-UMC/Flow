package com.example.client.ui.modal

import android.app.Dialog
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.client.api.HttpConnection
import com.example.client.databinding.ModalBoardDeleteBinding

class BoardDeleteModal(private val context : AppCompatActivity,val userId:Int, val detailId:List<Int>) {
    private lateinit var viewBinding :ModalBoardDeleteBinding
    private val dialog = Dialog(context)
    private val httpConnection : HttpConnection = HttpConnection()

    fun show(){
        viewBinding = ModalBoardDeleteBinding.inflate(context.layoutInflater)

        dialog.setContentView(viewBinding.root) //다이얼로그에 사용할 xml 파일을 불러옴
        dialog.setCancelable(false) //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, 700) //모달창 크기 지정

        //삭제하기 버튼 눌렀을 때
        viewBinding.modalDelete.setOnClickListener {

            var exList=HashMap<String, List<Int>>()
            exList["detailId"] = detailId
            //서버에서 삭제하기
            httpConnection.deleteDetail(userId,exList)

            dialog.dismiss()
        }
        //아니요 버튼 눌렀을 때
        viewBinding.modalCancel.setOnClickListener {
            dialog.dismiss()
        }
        // 'X' 눌렀을 때
        viewBinding.modalClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }


}