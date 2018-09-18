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
 * Created by surid on 6/29/16.
 */
public class RockPaperScissorPlayer {
    private static JFrame jFrame;
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

    private boolean isPlaying = true;
    private int playerMoveValue;
    private int computerMoveValue;


    public RockPaperScissorPlayer()throws IOException {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlaying && count < 5) {
                    playerMoveValue = 0;
                    out.println(playerMoveValue);
                    isPlaying = false;
                    gameStatus.setText("Wait for computer move");
                }
                else if(count >= 5)
                {
                    JOptionPane.showMessageDialog(null,"Please Start the game again!");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Computer does not give move");
                }
            }
        });
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlaying && count < 5) {
                    playerMoveValue = 1;
                    out.println(playerMoveValue);
                    isPlaying = false;
                    gameStatus.setText("Wait for computer move");
                }
                else if(count >= 5)
                {
                    JOptionPane.showMessageDialog(null,"Please Start the game again!");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Computer does not give move");
                }
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlaying && count < 5) {
                    playerMoveValue = 2;
                    out.println(playerMoveValue);
                    isPlaying = false;
                    gameStatus.setText("Wait for computer move");
                }
                else if(count >= 5)
                {
                    JOptionPane.showMessageDialog(null,"Please Start the game again!");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Computer does not give move");
                }
            }
        });

    }

    public void connectPlayer() throws IOException {
        serverSocket = new ServerSocket(9999);
        socket = serverSocket.accept();

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
        System.out.println("Computer Connected");
        gameStatus.setText("Your turn to start the game");

        OpponentPlayerMoveHandler server = new OpponentPlayerMoveHandler("client", socket);
        server.start();
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
                    computerMoveValue = Integer.parseInt(s);
                    if (playerMoveValue == computerMoveValue) {
                        pResult.setText((countPlayer) + "");
                        comResult.setText(countCom + "");
                        pMoveName.setText("Draw");
                        gameStatus.setText("Draw!");
                        out.println("draw");
                        count++;
                    } else if (computerMoveValue == 0 && playerMoveValue == 1 || computerMoveValue == 1 && playerMoveValue == 2 || computerMoveValue == 2 && playerMoveValue == 0) {
                        count++;
                        countPlayer++;
                        pMoveName.setText("Won this round!");
                        gameStatus.setText("Win!");
                        pResult.setText((countPlayer) + "");
                        comResult.setText(countCom + "");
                        out.println("lose");
                    } else {
                        count++;
                        countCom++;
                        pMoveName.setText("Lost this round!");
                        pResult.setText((countPlayer) + "");
                        comResult.setText(countCom + "");
                        gameStatus.setText("Lose!");
                        out.println("win");
                    }
                    gameStatus.setText("Now your turn");
                    isPlaying = true;
                }
                winnerguy();
                //socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String arg[]) throws IOException {
        RockPaperScissorPlayer rockPaperScissorPlayer = new RockPaperScissorPlayer();

        jFrame = new JFrame("Rock Paper Scissor Server!");
        jFrame.setLocation(700,100);
        jFrame.setContentPane(rockPaperScissorPlayer.rpspanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);

        rockPaperScissorPlayer.connectPlayer();
    }

    public void winnerguy()
    {
        if(countPlayer > countCom)
        {
            gameStatus.setText("Start the game again to Play!");
            winner.setText("Game end!\nYou won :D");
            pMoveName.setText("");
            count = 10;
        }
        else if(countPlayer == countCom)
        {
            gameStatus.setText("Start the game again to Play!");
            winner.setText("Game end!\nIt's a draw :D");
            pMoveName.setText("");
            count = 10;
        }
        else
        {
            gameStatus.setText("Start the game again to Play!");
            winner.setText("Game end!\nComputer won :D");
            pMoveName.setText("");
            count = 10;
        }
    }
}

