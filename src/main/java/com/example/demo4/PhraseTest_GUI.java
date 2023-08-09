package com.example.demo4;

import java.io.*;

import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PhraseTest_GUI extends Application{
    Label question = new Label("");
    Label hintmode = new Label("請選擇測驗模式");
    Label hintnum = new Label("請輸入測驗題數(<=20):");
    Label feedbackText = new Label("------feedback------");
    Label review = new Label("");
    TextField reply = new TextField("");
    TextField replyQN = new TextField("");
    Button quizcheck = new Button("check");
    Button quiznext = new Button("next");
    Button mode1 = new Button("測驗模式");
    Button mode2 = new Button("提示模式");
    static int number, quesnum=0, score=0, countquesnum=1, counterror=0, choosemode=1, j=0;
    static int []record=new int[20];
    static int []error=new int[20];
    static String []ans=new String[20];
    static String []ques=new String[20];
    static String a="", q="", inputa="";
    File sourceF = new File("phrase.txt");
    static Scanner sc=new Scanner("phrase.txt");
    static File file1 = new File("error.txt");

    public void start(Stage primaryStage)throws java.io.IOException
    {
        init();
        reply.setEditable(true);
        replyQN.setEditable(true);
        genQuestion();
        quizcheck.setOnAction(e->{
            inputa =  reply.getText();
            checkAns(); //Integer.parseInt
        });
        quiznext.setOnAction(e->{
            if(countquesnum<=quesnum)
            {
                reply.setText("");//clear reply textfield
                genQuestion();
            }
            else
            {
                try {
                    errorfile();

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                review.setText("測驗結束! 得分:"+score+"分, 答對率"+(j*100/quesnum)+"%\n錯題請見error.txt");
            }
        });
        mode1.setOnAction(e->{
            choosemode=1;
            quesnum= Integer.parseInt(replyQN.getText());
        });
        mode2.setOnAction(e->{
            choosemode=2;
            quesnum= Integer.parseInt(replyQN.getText());
        });

        HBox hbox1 = new HBox(question,reply,quizcheck,quiznext);
        HBox hbox3 = new HBox(hintmode,mode1, mode2);
        HBox hbox2 = new HBox(hintnum,replyQN);
        VBox vbox = new VBox(hbox2, hbox3, hbox1,feedbackText,review);
        hbox1.setSpacing(8);
        hbox2.setSpacing(8);
        hbox3.setSpacing(8);
        vbox.setSpacing(8);
        Scene scene = new Scene(vbox,500,250);
        primaryStage.setTitle("片語測驗");
        primaryStage.setScene(scene);
        primaryStage.show();
    }//start
    public void genQuestion()
    {
        number = (int)(Math.random()*1000) % 19;//隨機題號
        if(record[number]==0 )//判斷此題尚未出過
        {
            question.setText("第" + countquesnum + "題- " + ques[number] + ": ");
            record[number] = 1;//更新是否出過某題的陣列
            countquesnum++;//記錄此輪的總出題數
        }
    }//genQuestion
    public void checkAns()
    {
        if(inputa.equals(ans[number]))//答對
        {
            score+=10;
            j++;
            feedbackText.setText("CORRECT! 目前得分: "+score+"分");
        }
        else if(choosemode==1)//答錯
        {
            feedbackText.setText("INCORRECT! 目前得分: "+score+"分");
            error[counterror++]=number;//錯題題號紀錄至錯題陣列中
        }
        else//答錯
        {
            feedbackText.setText("INCORRECT! 目前得分: "+score+"分\nHint:"+ans[number].charAt(0)+"..."+ans[number].charAt(ans[number].length()-1));
            error[counterror++]=number;//錯題題號紀錄至錯題陣列中
        }
    }//checkAns

    public void init()throws java.io.IOException
    {
        if (!sourceF.exists()) {
            System.out.println("Source file does not exist");
            System.exit(2); }
        sc = new Scanner(sourceF);
        if (sc.hasNextLine())
            a= sc.nextLine();
        ans=a.split(", ");
        if (sc.hasNextLine())
            q= sc.nextLine();
        ques=q.split(", ");

        for(int b=0;b<20;b++)
            error[b]=21;
    }

    public static void errorfile() throws java.io.IOException
    {

        if (!file1.exists())
            file1.createNewFile();

        PrintWriter output1 = new PrintWriter(file1);//create PrintWriter objects
        for(int i=0;i<=error.length-1;i++)
        {
            if(error[i]!=21)
                output1.printf("%s   %s\n", ans[error[i]], ques[error[i]]);
        }
        output1.close();
    }

    public static void main(String[] args){
        launch(args);
    }//main
}//class
