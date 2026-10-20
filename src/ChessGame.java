// Chess Logic Lines: King=45, Queen=23, Rook=15, Bishop=15, Knight=18, Pawn=35 | Total=151 core logic lines
import java.util.*;

public class ChessGame {
    static class Board {
        char[][] b = {{'r','n','b','q','k','b','n','r'},{'p','p','p','p','p','p','p','p'},
                {'.','.','.','.','.','.','.','.'},{'.','.','.','.','.','.','.','.',},
                {'.','.','.','.','.','.','.','.'},{'.','.','.','.','.','.','.','.',},
                {'P','P','P','P','P','P','P','P'},{'R','N','B','Q','K','B','N','R'}};
        boolean wTurn = true, wKM = false, wQM = false, bKM = false, bQM = false;
        int[] enP = {-1, -1}, wK = {7,4}, bK = {0,4};
        ArrayList<String> hist = new ArrayList<>();
        
        boolean move(String m) {
            if(m.length() < 4) return false;
            int r1=8-(m.charAt(1)-'0'), c1=m.charAt(0)-'a', r2=8-(m.charAt(3)-'0'), c2=m.charAt(2)-'a';
            if(r1<0||r1>7||c1<0||c1>7||r2<0||r2>7||c2<0||c2>7) return false;
            char p = b[r1][c1];
            if(p=='.' || (wTurn && p>='a') || (!wTurn && p<='Z')) return false;
            if(!validMove(r1,c1,r2,c2,p)) return false;
            char cap = b[r2][c2];
            b[r2][c2] = p; b[r1][c1] = '.';
            if(p=='K') wK = new int[]{r2,c2};
            if(p=='k') bK = new int[]{r2,c2};
            if(inCheck(wTurn)) { b[r1][c1]=p; b[r2][c2]=cap; if(p=='K')wK=new int[]{r1,c1}; if(p=='k')bK=new int[]{r1,c1}; return false; }
            if(p=='K'&&!wKM&&c1==4&&c2==6&&r1==7&&r2==7) { b[7][5]='R'; b[7][7]='.'; }
            if(p=='K'&&!wKM&&c1==4&&c2==2&&r1==7&&r2==7) { b[7][3]='R'; b[7][0]='.'; }
            if(p=='k'&&!bKM&&c1==4&&c2==6&&r1==0&&r2==0) { b[0][5]='r'; b[0][7]='.'; }
            if(p=='k'&&!bKM&&c1==4&&c2==2&&r1==0&&r2==0) { b[0][3]='r'; b[0][0]='.'; }
            if(p=='P'&&enP[0]==r2&&enP[1]==c2&&r2==2) b[3][c2]='.';
            if(p=='p'&&enP[0]==r2&&enP[1]==c2&&r2==5) b[4][c2]='.';
            enP = (p=='P'&&r1==6&&r2==4) ? new int[]{3,c2} : (p=='p'&&r1==1&&r2==3) ? new int[]{4,c2} : new int[]{-1,-1};
            if(p=='P'&&r2==0) b[r2][c2]='Q';
            if(p=='p'&&r2==7) b[r2][c2]='q';
            if(p=='K') wKM=true; if(p=='k') bKM=true;
            if(p=='R'&&r1==7&&c1==0) wQM=true; if(p=='R'&&r1==7&&c1==7) wKM=true;
            if(p=='r'&&r1==0&&c1==0) bQM=true; if(p=='r'&&r1==0&&c1==7) bKM=true;
            hist.add(m); wTurn = !wTurn;
            return true;
        }
        
        boolean validMove(int r1, int c1, int r2, int c2, char p) {
            char t = b[r2][c2];
            boolean w = p<='Z';
            if((w && t<='Z' && t>='A') || (!w && t>='a' && t<='z')) return false;
            char lp = Character.toLowerCase(p);
            if(lp=='p') {
                int d = w ? -1 : 1, sr = w ? 6 : 1;
                if(c1==c2 && b[r2][c2]=='.' && r2==r1+d) return true;
                if(c1==c2 && r1==sr && b[r2][c2]=='.' && b[r1+d][c1]=='.' && r2==r1+2*d) return true;
                if(Math.abs(c2-c1)==1 && r2==r1+d && (b[r2][c2]!='.' || (enP[0]==r2 && enP[1]==c2))) return true;
                return false;
            }
            if(lp=='n') return (Math.abs(r2-r1)==2 && Math.abs(c2-c1)==1) || (Math.abs(r2-r1)==1 && Math.abs(c2-c1)==2);
            if(lp=='b') return Math.abs(r2-r1)==Math.abs(c2-c1) && clear(r1,c1,r2,c2);
            if(lp=='r') return (r1==r2 || c1==c2) && clear(r1,c1,r2,c2);
            if(lp=='q') return ((r1==r2||c1==c2) || Math.abs(r2-r1)==Math.abs(c2-c1)) && clear(r1,c1,r2,c2);
            if(lp=='k') {
                if(Math.abs(r2-r1)<=1 && Math.abs(c2-c1)<=1) return true;
                if(w && !wKM && r1==7 && r2==7 && c1==4) {
                    if(c2==6 && !wKM && b[7][5]=='.' && b[7][6]=='.' && b[7][7]=='R' && !attackedBy(7,4,false) && !attackedBy(7,5,false) && !attackedBy(7,6,false)) return true;
                    if(c2==2 && !wQM && b[7][1]=='.' && b[7][2]=='.' && b[7][3]=='.' && b[7][0]=='R' && !attackedBy(7,4,false) && !attackedBy(7,3,false) && !attackedBy(7,2,false)) return true;
                }
                if(!w && !bKM && r1==0 && r2==0 && c1==4) {
                    if(c2==6 && !bKM && b[0][5]=='.' && b[0][6]=='.' && b[0][7]=='r' && !attackedBy(0,4,true) && !attackedBy(0,5,true) && !attackedBy(0,6,true)) return true;
                    if(c2==2 && !bQM && b[0][1]=='.' && b[0][2]=='.' && b[0][3]=='.' && b[0][0]=='r' && !attackedBy(0,4,true) && !attackedBy(0,3,true) && !attackedBy(0,2,true)) return true;
                }
                return false;
            }
            return false;
        }
        
        boolean clear(int r1, int c1, int r2, int c2) {
            int dr = Integer.compare(r2,r1), dc = Integer.compare(c2,c1), r = r1+dr, c = c1+dc;
            while(r!=r2 || c!=c2) { if(b[r][c]!='.') return false; r+=dr; c+=dc; }
            return true;
        }
        
        boolean inCheck(boolean w) { int[] k = w ? wK : bK; return attackedBy(k[0], k[1], !w); }
        
        boolean attackedBy(int r, int c, boolean byWhite) {
            for(int i=0;i<8;i++) for(int j=0;j<8;j++) {
                char p = b[i][j];
                if(p=='.' || (byWhite && p>='a') || (!byWhite && p<='Z')) continue;
                char lp = Character.toLowerCase(p);
                if(lp=='p') {
                    int d = (p=='P') ? -1 : 1;
                    if(r==i+d && Math.abs(c-j)==1) return true;
                } else if(lp=='n') { if((Math.abs(r-i)==2&&Math.abs(c-j)==1)||(Math.abs(r-i)==1&&Math.abs(c-j)==2)) return true;
                } else if(lp=='b') { if(Math.abs(r-i)==Math.abs(c-j)&&clear(i,j,r,c)) return true;
                } else if(lp=='r') { if((i==r||j==c)&&clear(i,j,r,c)) return true;
                } else if(lp=='q') { if(((i==r||j==c)||Math.abs(r-i)==Math.abs(c-j))&&clear(i,j,r,c)) return true;
                } else if(lp=='k') { if(Math.abs(r-i)<=1&&Math.abs(c-j)<=1) return true; }
            }
            return false;
        }
        
        boolean hasLegalMoves(boolean w) {
            for(int r1=0;r1<8;r1++) for(int c1=0;c1<8;c1++) {
                char p = b[r1][c1];
                if(p=='.' || (w && p>='a') || (!w && p<='Z')) continue;
                for(int r2=0;r2<8;r2++) for(int c2=0;c2<8;c2++) {
                    if(!validMove(r1,c1,r2,c2,p)) continue;
                    char cap = b[r2][c2]; int[] ok = (p=='K')?wK:(p=='k')?bK:null;
                    b[r2][c2]=p; b[r1][c1]='.';
                    if(p=='K') wK=new int[]{r2,c2}; if(p=='k') bK=new int[]{r2,c2};
                    boolean safe = !inCheck(w);
                    b[r1][c1]=p; b[r2][c2]=cap;
                    if(ok!=null) { if(p=='K') wK=ok; if(p=='k') bK=ok; }
                    if(safe) return true;
                }
            }
            return false;
        }
        
        void print() {
            System.out.println("\n╔═══════════════════════════════╗");
            System.out.println("║   a   b   c   d   e   f   g   h ║");
            System.out.println("╠═══════════════════════════════╣");
            for(int i=0;i<8;i++) { 
                System.out.print("║ "+(8-i)+"│");
                for(int j=0;j<8;j++) {
                    char p = b[i][j];
                    String sym = getPieceSymbol(p);
                    System.out.print(sym + "│");
                }
                System.out.println((8-i)+" ║");
            }
            System.out.println("╠═══════════════════════════════╣");
            System.out.println("║   a   b   c   d   e   f   g   h ║");
            System.out.println("╚═══════════════════════════════╝");
        }
        
        String getPieceSymbol(char p) {
            switch(p) {
                case 'K': return " ♔ ";
                case 'Q': return " ♕ ";
                case 'R': return " ♖ ";
                case 'B': return " ♗ ";
                case 'N': return " ♘ ";
                case 'P': return " ♙ ";
                case 'k': return " ♚ ";
                case 'q': return " ♛ ";
                case 'r': return " ♜ ";
                case 'b': return " ♝ ";
                case 'n': return " ♞ ";
                case 'p': return " ♟ ";
                default: return "   ";
            }
        }
        
        int eval() {
            int s=0; for(int i=0;i<8;i++) for(int j=0;j<8;j++) {
                char p=b[i][j]; int v=0;
                if(p=='.') continue;
                char lp=Character.toLowerCase(p);
                if(lp=='p')v=100; else if(lp=='n'||lp=='b')v=320; else if(lp=='r')v=500; else if(lp=='q')v=900; else if(lp=='k')v=20000;
                s += (p>='a')?-v:v;
            }
            return s;
        }
    }
    
    static int minimax(Board b, int d, int a, int beta, boolean max) {
        if(d==0) return b.eval();
        if(max) {
            int m=-999999; for(int r1=0;r1<8;r1++) for(int c1=0;c1<8;c1++) {
                char p=b.b[r1][c1]; if(p=='.'||p>='a') continue;
                for(int r2=0;r2<8;r2++) for(int c2=0;c2<8;c2++) {
                    Board nb = copy(b); if(!nb.move(""+(char)('a'+c1)+(8-r1)+(char)('a'+c2)+(8-r2))) continue;
                    int e=minimax(nb,d-1,a,beta,false); m=Math.max(m,e); a=Math.max(a,e); if(beta<=a) return m;
                }
            }
            return m==-999999?-20000:m;
        } else {
            int m=999999; for(int r1=0;r1<8;r1++) for(int c1=0;c1<8;c1++) {
                char p=b.b[r1][c1]; if(p=='.'||p<='Z') continue;
                for(int r2=0;r2<8;r2++) for(int c2=0;c2<8;c2++) {
                    Board nb=copy(b); if(!nb.move(""+(char)('a'+c1)+(8-r1)+(char)('a'+c2)+(8-r2))) continue;
                    int e=minimax(nb,d-1,a,beta,true); m=Math.min(m,e); beta=Math.min(beta,e); if(beta<=a) return m;
                }
            }
            return m==999999?20000:m;
        }
    }
    
    static String bestMove(Board b, boolean w) {
        String bm=null; int bs=w?-999999:999999;
        for(int r1=0;r1<8;r1++) for(int c1=0;c1<8;c1++) {
            char p=b.b[r1][c1]; if(p=='.'||(w&&p>='a')||(!w&&p<='Z')) continue;
            for(int r2=0;r2<8;r2++) for(int c2=0;c2<8;c2++) {
                String m=""+(char)('a'+c1)+(8-r1)+(char)('a'+c2)+(8-r2);
                Board nb=copy(b); if(!nb.move(m)) continue;
                int s=minimax(nb,3,-999999,999999,!w);
                if((w&&s>bs)||(!w&&s<bs)) { bs=s; bm=m; }
            }
        }
        return bm;
    }
    
    static Board copy(Board b) {
        Board n=new Board(); for(int i=0;i<8;i++) n.b[i]=b.b[i].clone();
        n.wTurn=b.wTurn; n.wKM=b.wKM; n.wQM=b.wQM; n.bKM=b.bKM; n.bQM=b.bQM;
        n.enP=b.enP.clone(); n.wK=b.wK.clone(); n.bK=b.bK.clone(); n.hist=new ArrayList<>(b.hist);
        return n;
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Board board = new Board();
        
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       WELCOME TO CHESS GAME!             ║");
        System.out.println("║   You (♙ White) vs AI (♟ Black)          ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║ Commands:                                ║");
        System.out.println("║  • Enter moves like: e2e4                ║");
        System.out.println("║  • Type 'hint' for a suggested move     ║");
        System.out.println("║  • Type 'quit' to exit                   ║");
        System.out.println("║  • Type 'history' to see move history   ║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        board.print();
        
        while(true) {
            // Check for game over
            if(board.inCheck(board.wTurn) && !board.hasLegalMoves(board.wTurn)) {
                System.out.println("\n🏆 " + (board.wTurn ? "BLACK WINS BY CHECKMATE!" : "WHITE WINS BY CHECKMATE!") + " 🏆");
                break;
            }
            if(!board.inCheck(board.wTurn) && !board.hasLegalMoves(board.wTurn)) {
                System.out.println("\n⚖ STALEMATE - DRAW! ⚖");
                break;
            }
            
            if(board.wTurn) {
                // Player's turn (White)
                if(board.inCheck(true)) {
                    System.out.println("\n⚠️  CHECK! Your king is in danger!");
                }
                System.out.print("\n♙ WHITE's move (e.g., e2e4): ");
                String input = sc.nextLine().trim().toLowerCase();
                
                if(input.equals("quit")) {
                    System.out.println("Thanks for playing!");
                    break;
                }
                
                if(input.equals("hint")) {
                    System.out.print("Calculating best move... ");
                    String hint = bestMove(board, true);
                    System.out.println("Suggested move: " + hint);
                    continue;
                }
                
                if(input.equals("history")) {
                    System.out.println("\nMove History:");
                    for(int i = 0; i < board.hist.size(); i++) {
                        if(i % 2 == 0) System.out.print((i/2 + 1) + ". ");
                        System.out.print(board.hist.get(i) + " ");
                        if(i % 2 == 1) System.out.println();
                    }
                    if(board.hist.size() % 2 == 1) System.out.println();
                    continue;
                }
                
                if(!board.move(input)) {
                    System.out.println("❌ Invalid move! Try again.");
                    continue;
                }
                
                board.print();
                
            } else {
                // AI's turn (Black)
                if(board.inCheck(false)) {
                    System.out.println("\n⚠️  AI is in CHECK!");
                }
                System.out.println("\n♟ BLACK (AI) is thinking...");
                
                long startTime = System.currentTimeMillis();
                String aiMove = bestMove(board, false);
                long elapsed = System.currentTimeMillis() - startTime;
                
                if(aiMove == null) {
                    System.out.println("AI has no legal moves!");
                    break;
                }
                
                board.move(aiMove);
                System.out.println("♟ BLACK played: " + aiMove + " (calculated in " + elapsed + "ms)");
                
                board.print();
            }
        }
        
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║           GAME OVER!                     ║");
        System.out.println("║   Final move count: " + board.hist.size() + "                      ║");
        System.out.println("╚══════════════════════════════════════════╝");
        
        sc.close();
    }
}
