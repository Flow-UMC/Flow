package com.example.client.ui.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.SearchView
import com.example.client.data.adapter.CategoryViewAdapter
import com.example.client.R
import com.example.client.data.AppDatabase
import com.example.client.data.Category
import com.example.client.data.Keyword
import com.example.client.databinding.ActivityChangeCategoryBinding
import com.example.client.ui.board.ListDetailActivity
import kotlin.collections.List

class ChangeCategoryActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityChangeCategoryBinding
    private lateinit var categoryList : ArrayList<Category>
    private lateinit var searchCategoryList : ArrayList<Category>
    private lateinit var adapter: CategoryViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityChangeCategoryBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        val listDetailsIntent = intent
        val listId:Int = listDetailsIntent.getIntExtra("listId",1)
        val categoryType : Int = listDetailsIntent.getIntExtra("typeId",1)
        val selectedCategoryPosition: Int = listDetailsIntent.getIntExtra("order",0)
        val roomDb = AppDatabase.getCategoryInstance(this)
        val keywordDb = AppDatabase.getKeywordInstance(this)

        categoryList = roomDb!!.CategoryDao().selectAll().filter { category -> category.typeId==categoryType } as ArrayList<Category>
        adapter = CategoryViewAdapter(this, categoryList, selectedCategoryPosition)
        viewBinding.changeCategoryList.adapter = adapter
        viewBinding.changeCategoryButton.text = getText(R.string.finish_button)

        // 완료하기 버튼 클릭
        viewBinding.changeCategoryButton.setOnClickListener(){
            // 내역의 카테고리 변경
            val listDb = AppDatabase.getListInstance(this)
            val selectedCategoryId = roomDb.CategoryDao().selectByName((viewBinding.changeCategoryList.focusedChild as AppCompatButton).text.toString())
            val prevCategoryId = listDb!!.ListDao().selectById(listId).categoryId
            val isKeyword : Keyword = keywordDb!!.KeywordDao().selectByKeyword(listDb!!.ListDao().selectById(listId).shop)
            if((isKeyword != null)  ){
                // 키워드 삭제
                if(isKeyword.isUserCreated){
                    keywordDb.KeywordDao().deleteKeyword(prevCategoryId,listDb!!.ListDao().selectById(listId).shop)
                    listDb.ListDao().updateIsKeywordIncluded(listId, false)
                }
            }
            listDb!!.ListDao().updateCategory(listId,selectedCategoryId)

            // 내역 상세 화면으로 이동. 내역 id를 담아서 전송
            val intent = Intent(this, ListDetailActivity::class.java)
            intent.putExtra("listId",listId)
            startActivity(intent)
            finish()
        }

        // 내역 상세 화면으로 이동
        viewBinding.changeCategoryBackButton.setOnClickListener(){
            super.onBackPressed()
        }
        // 카테고리 추가 화면으로 이동
        viewBinding.changeCategoryAddButton.setOnClickListener(){
            val intent = Intent(this, AddCategoryActivity::class.java)
            intent.putExtra("listId",listId)
            intent.putExtra("typeId",categoryType)
            intent.putExtra("order",selectedCategoryPosition)
            startActivity(intent)
        }
        // 카테고리 검색
        viewBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (roomDb != null) {
                    searchCategoryList =
                        categoryList.filter { category -> category.name.contains(newText.toString().trim()) } as ArrayList<Category>
                    adapter.updateCategoryList(searchCategoryList)
                    viewBinding.changeCategoryList.adapter = adapter
                }
                return true
            }
        })

    }
}

