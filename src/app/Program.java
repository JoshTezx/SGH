package app;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.formdev.flatlaf.*;

import view.Login;

public class Program {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			JFrame.setDefaultLookAndFeelDecorated(false);
		} catch (Exception e) {
			System.err.println("No se pudo aplicar la textura");
		}
		
		JFrame f = new JFrame("Login");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setContentPane(new Login());
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}
