package it.unibo.ai.didattica.competition.tablut.wolfgang;

import java.io.IOException;
import java.net.UnknownHostException;
import it.unibo.ai.didattica.competition.tablut.domain.*;
import it.unibo.ai.didattica.competition.tablut.algorithms.IterativeDeepeningWithOrdering;
import it.unibo.ai.didattica.competition.tablut.client.TablutClient;


public class TablutArtificialClient extends TablutClient {
	private int timeout;
	private String serverIp;

	public TablutArtificialClient(String player, String name, int timeout, String ipAddress) throws UnknownHostException, IOException {
		super(player, name, timeout, ipAddress);
		this.timeout = timeout;
		this.serverIp = ipAddress;

	}

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		String role = "";
		String name = "Wolfgang";
		String ipAddress = "localhost";
		int timeout = 60;
	
		if (args.length < 1) {
			System.out.println("You must specify which player you are (WHITE or BLACK)");
			System.exit(-1);
		} else {
			System.out.println(args[0]);
			role = (args[0]);
		}
		if (args.length == 2) {
			System.out.println(args[1]);
			timeout = Integer.parseInt(args[1]);
		}
		if (args.length == 3) {
			ipAddress = args[2];
		}
		System.out.println("Selected client: " + args[0]);

		TablutArtificialClient client = new TablutArtificialClient(role, name, timeout, ipAddress);
		client.run();
	}

	@Override
    public void run() {

        // send name of your group to the server saved in variable "name"
        try {
            this.declareName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // set type of state and WHITE must do the first player
        State state = new StateTablut();
        state.setTurn(State.Turn.WHITE);

        // set type of game
        GameAshtonTablut tablutGame = new GameAshtonTablut(0, -1, "logs", "white_ai", "black_ai");;


        // attributes depends to parameters passed to main
        System.out.println("Player: " + (this.getPlayer().equals(State.Turn.BLACK) ? "BLACK" : "WHITE" ));
        System.out.println("Timeout: " + this.timeout +" s");
        System.out.println("Server: " + this.serverIp);

        // still alive until you are playing
        while (true) {

            // update the current state from the server
            try {
                this.read();
            } catch (ClassNotFoundException | IOException e1) {
                e1.printStackTrace();
                System.exit(1);
            }

            // print current state
            System.out.println("Current state:");
            state = this.getCurrentState();
            System.out.println(state.toString());

            // if i'm WHITE
            if (this.getPlayer().equals(State.Turn.WHITE)) {

                // if is my turn (WHITE)
                if (state.getTurn().equals(StateTablut.Turn.WHITE)) {

                    // search the best move in search tree
                    Action a = searchForBestAction(tablutGame, state);

                    System.out.println("\nAction selected: " + a.toString());
                    try {
                        this.write(a);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }

                }

                // if is turn of oppenent (BLACK)
                else if (state.getTurn().equals(StateTablut.Turn.BLACK)) {
                    System.out.println("Waiting for your opponent move...\n");
                }
                // if I WIN
                else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
                    System.out.println("YOU WIN!");
                    System.exit(0);
                }
                // if I LOSE
                else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
                    System.out.println("YOU LOSE!");
                    System.exit(0);
                }
                // if DRAW
                else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
                    System.out.println("DRAW!");
                    System.exit(0);
                }

            }
            // if i'm BLACK
            else {

                // if is my turn (BLACK)
                if (this.getCurrentState().getTurn().equals(StateTablut.Turn.BLACK)) {

                    System.out.println("\nSearching a suitable move... ");

                    // search the best move in search tree
                    Action a = searchForBestAction(tablutGame, state);

                    System.out.println("\nAction selected: " + a.toString());
                    try {
                        this.write(a);
                    } catch (ClassNotFoundException | IOException e) {
                        e.printStackTrace();
                    }

                }


                // if is turn of oppenent (WHITE)
                else if (state.getTurn().equals(StateTablut.Turn.WHITE)) {
                    System.out.println("Waiting for your opponent move...\n");
                }

                // if I LOSE
                else if (state.getTurn().equals(StateTablut.Turn.WHITEWIN)) {
                    System.out.println("YOU LOSE!");
                    System.exit(0);
                }

                // if I WIN
                else if (state.getTurn().equals(StateTablut.Turn.BLACKWIN)) {
                    System.out.println("YOU WIN!");
                    System.exit(0);
                }

                // if DRAW
                else if (state.getTurn().equals(StateTablut.Turn.DRAW)) {
                    System.out.println("DRAW!");
                    System.exit(0);
                }
            }
        }
    }

    private Action searchForBestAction(GameAshtonTablut tablutGame, State state) {
        double utilMin = -1.0; 
        double utilMax = 1.0;  
        int time = 30; // Tempo limite in secondi

        IterativeDeepeningWithOrdering search = new IterativeDeepeningWithOrdering(tablutGame, utilMin, utilMax, time);

        return search.makeDecision(state);
    }

}