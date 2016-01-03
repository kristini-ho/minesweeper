
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.net.URL;
import javax.sound.sampled.*;
import java.util.Random;

public class mines extends javax.swing.JFrame {
    
    private JMenuBar theMenuBar;   private JMenu fileGame, fileOptions, fileHelp;    
    private JMenuItem menuPlay, menuNewGame, menuAbout, menuExit, menuMines, menuAutoplay, menuBombStyle;   
    private JPanel minefield, mineCount, timeElapsed;   private JButton smileyButton;  
    private JTextPane pane, about;   private JScrollPane scroll;
    private BufferedImage flag, bomb; 
    private javax.swing.Timer timer;
    private AudioInputStream audioIn; private Clip clip;
    private boolean newGame, gameOver, autoplay, revealSome, toFlag;  
    private int time, neighbors, revealed, autorow, autocol, numMines; 
    private LineBorder lineborder;      
    private JPanel[][] grid;  private boolean[][] flags, bombs, unmarked, h; 
    private Random random = new Random();
    private Mouse mouse = new Mouse();  
         
    public mines() {   
        newGame = true;
        gameOver = autoplay = revealSome = toFlag = false;
        neighbors = revealed = time = 0; 
        numMines = 20;    
        flags = new boolean[20][20];
        bombs = new boolean[20][20];
        h = new boolean[20][20];
        unmarked = new boolean[20][20];
        grid = new JPanel[20][20];  
        
        initComponents();        
        
        for(int i = 0; i < 400; i++){
            JPanel a = new JPanel(){
                public void paintComponent(Graphics g){
                    super.paintComponent(g);
                    if(getBackground().getBlue() == 129) g.drawImage(flag, 0, 0, 21, 21, null);
                    if(gameOver == true && getBackground().getRed() == 129 || getBackground().getRed() == 193)
                        g.drawImage(bomb, 2, 2, 19, 19, null);                    
                }
            };
            a.setBackground(Color.GRAY); 
            a.setBorder(new BevelBorder(BevelBorder.RAISED));
            grid[i/20][i%20] = a;
            minefield.add(grid[i/20][i%20]);
        }  
        
        timer = new javax.swing.Timer(500, new ActionListener(){
            public void actionPerformed(ActionEvent e){
               timeElapsed.repaint();
               if(autoplay) autoplay();
            }
        });
        
        panes();
        images(); 
        sound();
    }
    private void sound(){
        try{
            audioIn = AudioSystem.getAudioInputStream(new File("bombsound2.wav"));
            clip = AudioSystem.getClip();
        }catch(Exception e){;}
    }    
    private void images(){
        try{
            flag = ImageIO.read(new File("images/flag.png"));
            bomb = ImageIO.read(new File("images/bomb.png"));
        }catch(IOException e){;}
    }
    private void panes(){
        try{
            about = new JTextPane();
            about.setPage(new URL("file:About.html"));
            about.setEditable(false);
            about.setPreferredSize(new Dimension(200, 240));
            pane = new JTextPane();
            pane.setPage(new URL("file:HowToPlay.html"));
            pane.setEditable(false);
            scroll = new JScrollPane(pane);
            scroll.setPreferredSize(new Dimension(500, 400));
        }catch(Exception e){;}
    }
    private void newGrid(){
        timer.stop(); 
        time = revealed = 0;
        newGame = true;
        gameOver = autoplay = false;
        for(int i = 0; i < 400; i++){
            grid[i/20][i%20].removeAll();
            grid[i/20][i%20].setBackground(Color.GRAY);
            grid[i/20][i%20].setBorder(new BevelBorder(BevelBorder.RAISED));
            flags[i/20][i%20] = bombs[i/20][i%20] = false;
            unmarked[i/20][i%20] = true;
        }        
        timeElapsed.repaint();
        sound();
    }
    @SuppressWarnings("unchecked")
    private void initComponents() {        
        lineborder = new LineBorder(Color.BLACK);
        minefield = new JPanel();
        theMenuBar = new javax.swing.JMenuBar();
        fileGame = new javax.swing.JMenu();
        menuNewGame = new javax.swing.JMenuItem();
        menuExit = new javax.swing.JMenuItem();
        menuAutoplay = new JMenuItem();
        fileOptions = new javax.swing.JMenu();
        menuMines = new javax.swing.JMenuItem();
        smileyButton = new javax.swing.JButton();
        fileHelp = new javax.swing.JMenu();
        menuPlay = new javax.swing.JMenuItem();
        menuAbout = new javax.swing.JMenuItem();
        menuBombStyle = new JMenuItem();        
        minefield.addMouseListener(mouse);
        mineCount = new javax.swing.JPanel(){
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                Font font = new Font("Default", Font.PLAIN, 15);
                g.setFont(font);
                g.drawString("" + numMines, 60, 47);
            }
        };
        timeElapsed = new javax.swing.JPanel(){
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                Font font = new Font("Default", Font.PLAIN, 15);
                g.setFont(font);
                g.drawString("" + (time++)/2, 70, 47);
            }
        };
        smileyButton.addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e){
               newGrid();
            }
        });        
        menuMines.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
               String s = JOptionPane.showInputDialog("Number mines? (Maximum 391)", 20);
               if(s != null){                
                   numMines = Integer.parseInt(s); 
                   if(numMines >= 392) numMines = 391;
                   newGrid();
                   mineCount.repaint();
               }
            }
        });  
        
        // bunch of messy-looking layout stuff from NetBeans below        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Minesweeper");
        setBounds(new java.awt.Rectangle(300, 80, 500, 620));
        setPreferredSize(new java.awt.Dimension(500, 620));
        setResizable(false);
        getContentPane().setLayout(null);
        minefield.setLayout(new java.awt.GridLayout(20, 20));
        getContentPane().add(minefield);
        minefield.setBounds(25, 15, 440, 440);
        mineCount.setBackground(null);
        mineCount.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder
            (new java.awt.Color(0, 0, 0)), "Mines", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));
        mineCount.setForeground(null);
        javax.swing.GroupLayout mineCountLayout = new javax.swing.GroupLayout(mineCount);
        mineCount.setLayout(mineCountLayout);
        mineCountLayout.setHorizontalGroup(
            mineCountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );
        mineCountLayout.setVerticalGroup(
            mineCountLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 59, Short.MAX_VALUE)
        );
        getContentPane().add(mineCount);
        mineCount.setBounds(300, 470, 140, 80);
        timeElapsed.setBackground(null);
        timeElapsed.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(
            new java.awt.Color(0, 0, 0)), "Time Elapsed", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION, null, new java.awt.Color(0, 0, 0)));
        timeElapsed.setForeground(null);
        javax.swing.GroupLayout timeElapsedLayout = new javax.swing.GroupLayout(timeElapsed);
        timeElapsed.setLayout(timeElapsedLayout);
        timeElapsedLayout.setHorizontalGroup(
            timeElapsedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        timeElapsedLayout.setVerticalGroup(
            timeElapsedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 59, Short.MAX_VALUE)
        );
        getContentPane().add(timeElapsed);
        timeElapsed.setBounds(30, 470, 150, 80);
        smileyButton.setBackground(null);
        smileyButton.setForeground(null);
        smileyButton.setIcon(new javax.swing.ImageIcon("images/smiley.png"));
        getContentPane().add(smileyButton);
        smileyButton.setBounds(220, 490, 40, 40);
        fileGame.setText("Game");
        menuNewGame.setText("New Game");       
        fileGame.add(menuNewGame);
        menuExit.setText("Exit");        
        fileGame.add(menuExit);
        theMenuBar.add(fileGame);
        fileOptions.setText("Options");
        menuMines.setText("Total Mines");
        fileOptions.add(menuMines);        
        menuBombStyle.setText("Style");
        fileOptions.add(menuBombStyle);
        menuAutoplay.setText("Autoplay");        
        fileOptions.add(menuAutoplay);
        theMenuBar.add(fileOptions);
        fileHelp.setText("Help");
        menuPlay.setText("How to Play");        
        fileHelp.add(menuPlay);
        menuAbout.setText("About");
        fileHelp.add(menuAbout);
        theMenuBar.add(fileHelp);
        
        // adding action listeners
        menuNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGrid();
            }
        });
        menuExit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        menuBombStyle.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                 String s = JOptionPane.showInputDialog("Enter a number:\n1) Classic 2) Red 3) Purple");
                 if(s != null){ int n = Integer.parseInt(s);
                   if(n == 2){
                         try{ bomb = ImageIO.read(new File("images/redflower.png")); }
                         catch(IOException x){;}
                   }else if(n == 3){
                         try{bomb = ImageIO.read(new File("images/littlepurple.png")); }
                         catch(IOException x){;}
                   }else if(n == 1){
                         try{ bomb = ImageIO.read(new File("images/bomb.png")); }
                        catch(IOException x){;}
                   }
               }
            }
        });
        menuAutoplay.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                newGrid();
                autoplay = true;
                timer.start();
            }
        });
        menuPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPlayActionPerformed(evt);
            }
        });
        menuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAboutActionPerformed(evt);
            }
        });        
        setJMenuBar(theMenuBar);
    }
    private void menuAboutActionPerformed(java.awt.event.ActionEvent evt) {
         JOptionPane.showMessageDialog(null, about, "About", JOptionPane.PLAIN_MESSAGE);
    }
    private void menuPlayActionPerformed(java.awt.event.ActionEvent evt) {
         JOptionPane.showMessageDialog(null, scroll, "How to Play", JOptionPane.PLAIN_MESSAGE);
    }
    private void highlight (int r, int c, boolean[][] array){
         if(inBound(r, c-1) && array[r][c-1] &&!flags[r][c-1] ){ grid[r][c-1].setBackground(Color.GREEN); h[r][c-1] = true; }
         if(inBound(r, c+1) && array[r][c+1] &&!flags[r][c+1] ){ grid[r][c+1].setBackground(Color.GREEN); h[r][c+1] = true; }
         if(inBound(r+1, c-1) && array[r+1][c-1] &&!flags[r+1][c-1] ) { grid[r+1][c-1].setBackground(Color.GREEN); h[r+1][c-1] = true; }
         if(inBound(r+1, c) && array[r+1][c] &&!flags[r+1][c] ) { grid[r+1][c].setBackground(Color.GREEN); h[r+1][c] = true; }
         if(inBound(r+1, c+1) && array[r+1][c+1] &&!flags[r+1][c+1] ) { grid[r+1][c+1].setBackground(Color.GREEN); h[r+1][c+1] = true; }
         if(inBound(r-1, c-1) && array[r-1][c-1] &&!flags[r-1][c-1] ) { grid[r-1][c-1].setBackground(Color.GREEN); h[r-1][c-1] = true; }
         if(inBound(r-1, c) && array[r-1][c] &&!flags [r-1][c]) { grid[r-1][c].setBackground(Color.GREEN); h[r-1][c] = true; }
         if(inBound(r-1, c+1) && array[r-1][c+1] &&!flags [r-1][c+1]) { grid[r-1][c+1].setBackground(Color.GREEN); h[r-1][c+1] = true; }
    }
    private boolean isBlank(boolean [][] h){
        boolean blank = true;
        for(int i = 0; i < 400; i++){
            if(h[i/20][i%20]) blank = false;
        }
        return blank;
    }    
    private void autoplay(){
            if(time%2 != 0){     // do the highlighting when time is odd
                h = new boolean[20][20];  // 2D array for highlighting
                if(revealSome){  
                    for(int i = 0; i < 400; i++){
                        if(!unmarked[i/20][i%20] && !flags[i/20][i%20] &&
                                countNeighbor(i/20, i%20, flags) == countNeighbor(i/20, i%20, bombs)){
                            highlight(i/20, i%20, unmarked); // all bombs are already flagged
                        }
                    }
                }
                if(toFlag){ 
                    for(int i = 0; i < 400; i++){
                        if(!unmarked[i/20][i%20] && !flags[i/20][i%20] &&
                                countNeighbor(i/20, i%20, unmarked) == (countNeighbor(i/20, i%20, bombs)-countNeighbor(i/20, i%20, flags))){                                    
                            highlight(i/20, i%20, unmarked); // # unrevealed neighbors equals # unflagged bombs
                        }
                    }
                }
                if(isBlank(h)){
                    toFlag = revealSome = false;
                    do{
                        autorow = random.nextInt(20);
                        autocol = random.nextInt(20);
                    }while(unmarked[autorow][autocol] == false);
                    grid[autorow][autocol].setBackground(Color.GREEN);
                    h[autorow][autocol] = true;
                }
            }
            else{     // do the "clicking" when time is even                
                if(toFlag){
                    for(int i = 0; i < 400; i++){
                        if(h[i/20][i%20])  mouse.rightClick(i/20, i%20);                          
                    }
                    toFlag = false; revealSome = true;
                }
                else if(revealSome){
                   for(int i = 0; i < 400; i++){
                        if(h[i/20][i%20])  mouse.leftClick(i/20, i%20);                          
                    }
                   toFlag = true; revealSome = false;
                }
                else{ mouse.leftClick(autorow, autocol); toFlag = true; }
            }
    }
    private class Mouse extends MouseAdapter{
        public void leftClick(int row, int col){
             if(newGame){ timer.start(); newGame = false; makeBombs(row, col); masterRecursion(row, col); }
             if(!flags[row][col]){
                 if(bombs[row][col]){
                      gameOver = true;
                      showBombs();
                      if(timer.isRunning()) JOptionPane.showMessageDialog(null, "Game Over!", "", JOptionPane.PLAIN_MESSAGE);
                      timer.stop();
                 }
                 else masterRecursion(row, col);
             }
        }
        public void rightClick(int row, int col){
            if(grid[row][col].getBackground().getGreen() == 128 || grid[row][col].getBackground() == Color.GREEN){
                if(flags[row][col]){
                     flags[row][col] = false;
                     unmarked[row][col] = true;
                     grid[row][col].setBackground(Color.GRAY);
                }
                 else{
                    flags[row][col] = true;
                    unmarked[row][col] = false;
                    grid[row][col].setBackground(new Color(128, 128, 129));
                }
            }
        }
        public void mousePressed(MouseEvent e){
            if(gameOver == false){
                int col = e.getX()/22;
                int row = e.getY()/22 ;
                if((e.getModifiersEx() & e.BUTTON1_DOWN_MASK) == e.BUTTON1_DOWN_MASK){
                    leftClick(row, col);
                }
                if((e.getModifiersEx() & e.BUTTON3_DOWN_MASK) == e.BUTTON3_DOWN_MASK){
                    rightClick(row, col);
                }
            }
        }
    }
    private void masterRecursion(int row, int col){
          if(revealed == 400-numMines){ timer.stop();
              JOptionPane.showMessageDialog(null, "You've won!", "", JOptionPane.PLAIN_MESSAGE);
              gameOver = true;
             }
          if((neighbors = countNeighbor(row, col, bombs)) > 0) {
                grid[row][col].setBackground(new Color(192, 193, 192));
                grid[row][col].setBorder(lineborder);
                grid[row][col].add(new JLabel("" + countNeighbor(row, col, bombs)));
                grid[row][col].revalidate();
                unmarked[row][col] = false;
                revealed++;
          }
          else{
              grid[row][col].setBackground(Color.LIGHT_GRAY);
              grid[row][col].setBorder(lineborder);
              unmarked[row][col] = false;
              revealed++;
             if(shouldReveal(row-1, col-1)) masterRecursion(row-1, col-1);   //diag left up
             if(shouldReveal(row-1, col)) masterRecursion(row-1, col);  //up
             if(shouldReveal(row-1, col+1)) masterRecursion(row-1, col+1);  //diag right up
             if(shouldReveal(row, col+1)) masterRecursion(row, col+1); //right
             if(shouldReveal(row + 1, col+1) ) masterRecursion(row+1, col+1); //diag right bottom
             if(shouldReveal(row+1, col) ) masterRecursion(row+1, col);   //bottom
             if(shouldReveal(row+1, col-1)) masterRecursion(row+1, col-1);  //diag left bottom
             if(shouldReveal(row, col-1) ) masterRecursion(row, col-1);   //left
          }
    }
    private boolean shouldReveal(int row, int col){
        return (inBound(row, col) && !bombs[row][col] && grid[row][col].getBackground() == Color.GRAY) ? true: false;
    }
    private int countNeighbor(int r, int c, boolean[][] array){
         int neighbors = 0;
         if(inBound(r, c-1) && array[r][c-1]) neighbors++;   
         if(inBound(r, c+1) && array[r][c+1]) neighbors++;
         if(inBound(r+1, c-1) && array[r+1][c-1]) neighbors++;
         if(inBound(r+1, c) && array[r+1][c]) neighbors++;
         if(inBound(r+1, c+1) && array[r+1][c+1]) neighbors++;
         if(inBound(r-1, c-1) && array[r-1][c-1]) neighbors++;
         if(inBound(r-1, c) && array[r-1][c]) neighbors++;
         if(inBound(r-1, c+1) && array[r-1][c+1]) neighbors++;
         return neighbors;
    }
    private boolean inBound(int r, int c){
         return ( r <= 19 && r >=0 && c <= 19 && c >= 0) ? true: false;
    }
    private boolean isNeighbor(int r, int c, int row, int col){
          if(col == c)
              if(row+1 == r || row-1 == r) return true;
          if(col == c+1)
              if(row+1 == r || row-1 == r || row == r) return true;
          if(col == c-1)
              if(row+1 == r || row-1 == r || row == r) return true;
         return false;
    }
    private void makeBombs(int r, int c){
        int row = 0;
        int col = 0;
        for(int i = 0; i < numMines; i++){
            do{
               row = random.nextInt(20);
               col = random.nextInt(20);
            }while((row == r && col == c) || bombs[row][col] || isNeighbor(row, col, r, c));
            // the above makes sure bombs are placed away from where the user first clicks
            bombs[row][col] = true;
            grid[row][col].setBackground(new Color(129, 128, 128));
        }
    }
    private void showBombs(){
        try{clip.open(audioIn);} catch(Exception e){;}
        clip.start();
        for(int i = 0; i < 400; i++){
            if(bombs[i/20][i%20]){
                grid[i/20][i%20].setBackground(new Color(193, 192, 192));
                grid[i/20][i%20].setBorder(new LineBorder(Color.BLACK));
            }
        }
    }
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mines().setVisible(true);
            }
        });
    }    
}
