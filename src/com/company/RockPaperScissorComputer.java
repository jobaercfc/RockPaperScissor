package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Surid on 8/9/2016.
 */
public class RockPaperScissorComputer {
    private JPanel panel1;
    private JButton button2;
    private JPanel rpspanel;
    private JLabel resultShow;
    private JButton button1;
    private JButton button3;
    private JLabel playerLabel;
    private JLabel comlabel;
    private JLabel pResult;
    private JLabel comResult;
    private JLabel winner;
    private JLabel gameStatus;
    private JLabel pMoveName;
    private JLabel cMoveName;

    private int countPlayer = 0;
    private int countCom = 0;
    private int count = 0;
    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private static JFrame jFrame;

    private int totalPoint=0;
    private boolean isPlaying = false;
    private int playerMoveValue;
    private int computerMoveValue;

    public RockPaperScissorComputer() throws IOException {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlaying) {
                    computerMoveValue = 0;
                    out.println(computerMoveValue);
                    isPlaying = false;
                    gameStatus.setText("Wait for player move");
                }
                else if(count >= 5)
                {
                    JOptionPane.showMessageDialog(null,"Please Start the game again!");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Player does not give move");
                }
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlaying) {
                    computerMoveValue = 1;
                    out.println(computerMoveValue);
                    isPlaying = false;
                    gameStatus.setText("Wait for player move");
                }
                else if(count >= 5)
                {
                    JOptionPane.showMessageDialog(null,"Please Start the game again!");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Player does not give move");
                }
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlaying) {
                    computerMoveValue = 2;
                    out.println(computerMoveValue);
                    isPlaying = false;
                    gameStatus.setText("Wait for player move");
                }
                else if(count >= 5)
                {
                    JOptionPane.showMessageDialog(null,"Please Start the game again!");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Player does not give move");
                }
            }
        });
    }

    public void connectPlayer() throws IOException {
        socket = new Socket("127.0.0.1", 9999);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
        System.out.println("Player Connected");
        gameStatus.setText("Connected.Wait for player move!");

        OpponentPlayerMoveHandler client = new OpponentPlayerMoveHandler("server", socket);
        client.start();
    }

    class OpponentPlayerMoveHandler extends Thread {

        private String name;
        private Socket socket;

        public OpponentPlayerMoveHandler(String name, Socket socket) {
            super(name);
            this.name = name;
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                while (count < 5) {
                    String s = in.readLine();
                    if (s.equals("win")) {
                        count++;
                        countCom++;
                        pResult.setText((countPlayer) + "");
                        comResult.setText(countCom + "");
                        cMoveName.setText("Won this round!");
                        gameStatus.setText("Wait for player move");
                    } else if (s.equals("lose")) {
                        count++;
                        countPlayer++;
                        pResult.setText((countPlayer) + "");
                        comResult.setText(countCom + "");
                        cMoveName.setText("Lost this round!");
                        gameStatus.setText("Wait for player move");
                    }else if(s.equals("draw")){
                        count++;
                        cMoveName.setText("Draw!");
                        gameStatus.setText("Wait for player move");
                    }
                    else if(s.equals("New")){
                        count = 0;
                        countCom = 0;
                        countPlayer = 0;
                        comResult.setText(countCom + "");
                        pResult.setText(countPlayer + "");
                        gameStatus.setText("");
                        resultShow.setText("Result");
                        winner.setText("New Game :D");
                    }
                    else
                    {
                        gameStatus.setText("Now your turn");
                        isPlaying = true;
                    }
                }
                winnerguy();
                //socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        RockPaperScissorComputer rockPaperScissorComputer = new RockPaperScissorComputer();

        jFrame = new JFrame("Rock Paper Scissor Client!");
        jFrame.setLocation(100,100);
        jFrame.setContentPane(rockPaperScissorComputer.rpspanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);

        rockPaperScissorComputer.connectPlayer();
    }

    public void winnerguy()
    {
        if(countPlayer > countCom)
        {
            gameStatus.setText("Start the game again to Play!");
            winner.setText("Game end!\nPlayer won :D");
            cMoveName.setText("");
            count = 10;
        }
        else if(countPlayer == countCom)
        {
            gameStatus.setText("Start the game again to Play!");
            winner.setText("Game end!\nIt's a draw :D");
            cMoveName.setText("");
            count = 10;
        }
        else
        {
            gameStatus.setText("Start the game again to Play!");
            winner.setText("Game end!\nYou won :D");
            cMoveName.setText("");
            count = 10;
        }
    }
}
