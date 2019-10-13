package com.kailang.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static List<String> list;
    private TextView tv_input,tv_result;
    private Button bt_0,bt_1,bt_2,bt_3,bt_4,bt_5,bt_6,bt_7,bt_8,bt_9,bt_clean,bt_delete,bt_sqrt,bt_result,bt_add,bt_subtract,bt_multiply,bt_divide,bt_point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState!=null){
            tv_input.setText(savedInstanceState.getString("tv_input"));
            tv_result.setText(savedInstanceState.getString("tv_result"));
        }
        //初始化view
        init();
    }


    private void init() {
        list = new ArrayList<>();
        tv_input = findViewById(R.id.tv_input);
        tv_result = findViewById(R.id.tv_result);

        tv_input.requestFocus();
        bt_0 = findViewById(R.id.button_0);
        bt_0.setOnClickListener(this);
        bt_1 = findViewById(R.id.button_1);
        bt_1.setOnClickListener(this);
        bt_2 = findViewById(R.id.button_2);
        bt_2.setOnClickListener(this);
        bt_3 = findViewById(R.id.button_3);
        bt_3.setOnClickListener(this);
        bt_4 = findViewById(R.id.button_4);
        bt_4.setOnClickListener(this);
        bt_5 = findViewById(R.id.button_5);
        bt_5.setOnClickListener(this);
        bt_6 = findViewById(R.id.button_6);
        bt_6.setOnClickListener(this);
        bt_7 = findViewById(R.id.button_7);
        bt_7.setOnClickListener(this);
        bt_8 = findViewById(R.id.button_8);
        bt_8.setOnClickListener(this);
        bt_9 = findViewById(R.id.button_9);
        bt_9.setOnClickListener(this);
        bt_point = findViewById(R.id.button_point);
        bt_point.setOnClickListener(this);

        bt_add = findViewById(R.id.button_add);
        bt_add.setOnClickListener(this);
        bt_subtract = findViewById(R.id.button_subtract);
        bt_subtract.setOnClickListener(this);
        bt_multiply = findViewById(R.id.button_multiply);
        bt_multiply.setOnClickListener(this);
        bt_divide = findViewById(R.id.button_divide);
        bt_divide.setOnClickListener(this);

        bt_sqrt = findViewById(R.id.button_sqrt);
        bt_sqrt.setOnClickListener(this);
        bt_clean = findViewById(R.id.button_clean);
        bt_clean.setOnClickListener(this);
        bt_result = findViewById(R.id.button_result);
        bt_result.setOnClickListener(this);
        bt_delete = findViewById(R.id.button_delete);
        bt_delete.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putString("tv_input",tv_input.toString());
        outState.putString("tv_result",tv_result.toString());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_clean:
                list.clear();
                tv_result.setText("");
                tv_input.setText("");
                break;
            case R.id.button_delete:
                if(!list.isEmpty()) {
                    list.remove(list.size()-1);
                    tv_input.setText(getData());
                }
                break;
            case R.id.button_result:
                tv_result.setText(getResult());
                break;
             default:
                 Button bt = findViewById(view.getId());
                 String bt_str = bt.getText().toString();

                 //第一个字符不能是运算符号，正负根号除外
                 if(list.isEmpty()&&bt_str.matches("[×÷]"))break;

                 //不能连续运算符号
                 if(!list.isEmpty())    //防止list越界
                     if(bt_str.matches("[-×÷\\+]")&&list.get(list.size() - 1).matches("[-×÷√\\+]")) break;

                 //一个数只能一个小数点
                 if(bt_str.equals(".")&&(getData().matches("\\d*\\.\\d*")||getData().matches("[-\\+]\\d*\\.\\d*"))){
                     Log.e("match_getData_list",getData());
                     break;
                 }
                 //根号预处理
                 if(bt_result.toString().matches("\\d")&&bt_str.equals("√"))list.add("×"); //前面执行过运算，得到的数之后加乘号
                 if(bt_str.equals("√")&&!list.isEmpty()&&list.get(list.size()-1).matches("\\d"))list.add("×");  //根号前带数，在数前面加乘号

                 list.add(bt.getText().toString());
                 tv_input.setText(getData());
                 break;
        }

    }

    //运算
    private static String getResult() {

        if(list.isEmpty())return "0";
        //最后一个不能为运算符
        if(list.get(list.size()-1).matches("[-×÷\\+]"))return "error";

        String str = getData();
        double res; //运算结果

        //clean the list
        for(int i=list.size()-1;i>=0;i--)list.remove(i);

        /*
        将运算符与数字分割
         */
        String t="";
        for(int i=0;i<str.length();i++) {
            if(str.charAt(i)!='×'&&str.charAt(i)!='÷'&&str.charAt(i)!='+'&&str.charAt(i)!='-'&&str.charAt(i)!='√') {
                t+=str.charAt(i);
            }else {
                if(t!="")
                    list.add(t);    //将数字添加进去
                t="";
                list.add(str.charAt(i)+""); //把运算符放进去
            }
            //把最后一个数放进去
            if(i==str.length()-1)list.add(t);
        }
        for(String a :list)
            Log.e("after_list:",a);
        //清除空字符
        for(int i=0;i<list.size();i++)
            if(list.get(i).equals(""))
                list.remove(i);

         //算术运算
        try {
//              //百分号运算
//            for(int i=1;i<list.size();i++)
//                if(list.get(i).equals("%")) {
//                    String a=Double.parseDouble(list.get(i-1))/100+"";
//                    if(i-2==0&&list.get(i-2).equals("-")) {
//                        a = "-" + a;
//                        list.remove(i-2);i--;
//                    }
//                    if(i-2==0&&list.get(i-2).equals("+")){
//                        list.remove(i-2);i--;
//                    }
//                    list.set(i-1, a);
//                    list.remove(i);i--;
//                }

            //根号运算
            for(int i=list.size()-1;i>0;i--){
                double a =-1;
                if(list.get(i-1).equals("√")){
                    double b =Double.parseDouble(list.get(i));
                    //单根号,连续单根号
                    list.set(i-1,Math.sqrt(b)+"");
                    Log.e("sqrt_set_list",list.get(i-1));
                    Log.e("sqrt_remove_list",list.get(i));
                    list.remove(i);
                }
            }
            //除法运算
            for(int i=1;i<list.size();i++)
                if(list.get(i).equals("÷")) {
                    double a=Double.parseDouble(list.get(i-1));
                    double b=Double.parseDouble(list.get(i+1));
                    if(b==0)return "除数不能为0";

                    list.set(i-1, (a/b)+"");
                    list.remove(i);
                    list.remove(i);i--;
                }
            //乘法运算
            for(int i=1;i<list.size();i++)
                if(list.get(i).equals("×")) {
                    double a=Double.parseDouble(list.get(i-1));
                    double b=Double.parseDouble(list.get(i+1));

                    list.set(i-1, (a*b)+"");
                    list.remove(i);
                    list.remove(i);i--;
                }
            //减法运算
            for(int i=0;i<list.size();i++)
                if(list.get(i).equals("-")) {
                    double a;
                    double b=Double.parseDouble(list.get(i+1));
                    if(i==0) {
                        a=0;
                        list.set(i, (a-b)+"");
                        list.remove(i+1);i--;
                    }
                    else{
                        a=Double.parseDouble(list.get(i-1));
                        list.set(i-1, (a-b)+"");
                        list.remove(i);
                        list.remove(i);i--;
                    }
                }
            //加法运算
            for(int i=0;i<list.size();i++)
                if(list.get(i).equals("+")) {
                    double a;
                    double b=Double.parseDouble(list.get(i+1));
                    if(i==0){
                        a=0;
                        list.set(i, (a+b)+"");
                        list.remove(i+1);i--;
                    }
                    else {
                        a=Double.parseDouble(list.get(i-1));
                        list.set(i-1, (a+b)+"");
                        list.remove(i);
                        list.remove(i);i--;
                    }
                }
            res=Double.parseDouble(list.get(0));
        }catch(Exception e) {
            return "error";
        }
        return res+"";
    }

    //将ArrayList转成String显示在输出栏
    private static String getData() {
        String t="";
        for(String s:list)
            t+=s;
        return t;
    }
}
