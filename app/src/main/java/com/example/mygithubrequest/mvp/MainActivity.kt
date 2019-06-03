package com.example.mygithubrequest.mvp

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.mygithubrequest.*
import com.example.mygithubrequest.data.DetailsPref
import com.github.nitrico.lastadapter.LastAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MyActivityPresenter.MyActivityView {

    lateinit var presenter: MyActivityPresenter
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MyActivityPresenter(this)
        presenter.setAdapter()
        initView()
    }

    //Initializing the views and adding the click action functionalities
    private fun initView() {
        defaultViews()
        add_fab.setOnClickListener { presenter.onFabClickEvent() }
        list_rv.hideFabOnScroll(add_fab)
    }

    /**
     * @param adapter is a Lastadapter
     * Setting up the adapter to recyclerview
     */
    override fun setUpAdapter(adapter: LastAdapter) {
        if (list_rv.adapter == null)
            list_rv.adapter = adapter
    }

    override fun handleViews(show: Boolean) {
        val nameAndRepo = "${DetailsPref.ownerName}/${DetailsPref.repoName}"
        user_name_repo_tv.text = nameAndRepo
        user_name_repo_tv.visible()
        if (show) {
            empty_tv.gone()
        } else {
            empty_tv.text = "No results found"
            empty_tv.visible()
        }
    }

    /**
     * Showing the default views once the screen launches
     */
    private fun defaultViews() {
        user_name_repo_tv.gone()
        val emptyText = "To get the details please click\n on '+' fab button"
        empty_tv.text = emptyText
        empty_tv.visible()
    }

    /**
     * Initializing the custom dialog and displaying it.
     * handling the views
     */
    override fun showDetailsDialog() {

        dialog = Dialog(this, R.style.NewActivityDialogStyle);
        dialog!!.setContentView(R.layout.details_dialog)
        dialog?.window?.attributes?.windowAnimations = R.style.DetailsDialogAnimation
        dialog?.setCanceledOnTouchOutside(false)

        val ownerName = dialog?.findViewById<EditText>(R.id.owner_name_et)
        val ownerRepo = dialog?.findViewById<EditText>(R.id.owner_repo_et)
        val cancel = dialog?.findViewById<ImageButton>(R.id.cancel_btn)
        val save = dialog?.findViewById<ImageButton>(R.id.save_btn)

        save?.setOnClickListener {
            it.hideKeyboard()
            empty_tv.gone()
            presenter.getRequest(ownerName?.text.toString().trim(), ownerRepo?.text.toString().trim())
            dismissDialog()
        }

        cancel?.setOnClickListener { dismissDialog() }
        dialog?.show()

    }

    override fun onPause() {
        super.onPause()
        if (dialog != null && dialog!!.isShowing)
            dismissDialog()
    }

    //handling progress bar view
    override fun handleLoading(show: Boolean) {
        if (show)
            progress_bar.visible()
        else
            progress_bar.gone()
    }

    //Displaying the toast message using an extension function
    override fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    //diagloss dismiss
    override fun dismissDialog() {
        dialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.clearDisposable()
    }

}
