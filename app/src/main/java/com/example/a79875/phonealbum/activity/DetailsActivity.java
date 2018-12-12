package com.example.a79875.phonealbum.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.a79875.phonealbum.R;
import com.example.a79875.phonealbum.utils.Details;

import java.util.HashMap;

// 详细信息
public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton back;
    private TextView fileTitle;
    private TextView fileDate;
    private TextView fileType;
    private TextView fileSize;
    private TextView fileDimensions;
    private TextView filePath;
    HashMap<String, String> details = new HashMap<>();
    String fileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        fileUrl = getIntent().getStringExtra("fileUrl");
        details = Details.showDetails(this, fileUrl);

        initView();
    }

    private void initView() {
        back = findViewById(R.id.ib_details_back);
        fileTitle = findViewById(R.id.tv_file_title);
        fileDate = findViewById(R.id.tv_create_date);
        fileType = findViewById(R.id.tv_file_type);
        fileSize = findViewById(R.id.tv_file_size);
        fileDimensions = findViewById(R.id.tv_file_dimensions);
        filePath = findViewById(R.id.tv_file_path);

        fileTitle.setText(details.get("名称"));
        fileDate.setText(details.get("时间"));
        fileType.setText(details.get("类型"));
        fileSize.setText(details.get("大小"));
        fileDimensions.setText(details.get("尺寸"));
        filePath.setText(fileUrl);

        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_details_back:
                finish();
                break;
        }
    }
}
