import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.SwingConstants;
import javax.swing.JPanel;

public class GUI2 {
	private JFreeChart chart;
	private ChartPanel chartPanel = new ChartPanel(null);
	private Panel buttonPanel;
	private Panel statisticsPanel;
	private JFrame frame;
	private CmManager manager = null;
	private JTextField t1 = null;
	private JTextField t2 = null;
	private JTextField t3 = null;
	private JTextField t4 = null;
	private JTextField t5 = null;
	private JButton b4 = null;
	private JButton b5 = null;
	private JButton b6 = null;
	private JButton b7 = null;
	private JButton b8 = null;
	private JButton b9 = null;
	private JButton b10 = null;
	private JTextField t8;
	private JTextField t9;
	private JPanel mainPanel;
	private JTextField t10;
	private JTextField t11;
	private boolean fullScreen = false;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI2 window = new GUI2();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"An error has occurred.");
				}
			}
		});
	}

	public GUI2() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setTitle("Candle Master");
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());

		mainPanel = new JPanel();
		statisticsPanel = new Panel();
		statisticsPanel.setBackground(Color.BLACK);
		mainPanel.setBackground(Color.BLACK);
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
		// Initialize panel
		buttonPanel = new Panel();
		buttonPanel.setBackground(Color.BLACK);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new GridLayout());
		b4 = new JButton("Set prediction distance");
		b4.setFocusable(false);
		b4.setForeground(Color.WHITE);
		b4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int i = -1;
				String s = JOptionPane
						.showInputDialog("Enter new prediction distance:");
				if (s != null) {
					try {
						i = Integer.parseInt(s);
						manager.setPredictionDistance(i);
						manager.voteSkip();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"Invalid number entered");
					}
					manager.resetStatistics();
					refreshGui();
				}
			}
		});
		b5 = new JButton("Set history range");
		b5.setFocusable(false);
		b5.setForeground(Color.WHITE);
		b5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int i = -1;
				String s = JOptionPane
						.showInputDialog("Enter new history range:");
				if (s != null) {
					try {
						i = Integer.parseInt(s);
						manager.setHistoryRange(i);
						manager.voteSkip();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"Invalid number entered");
					}
					manager.resetStatistics();
					refreshGui();
				}
			}
		});
		b6 = new JButton("Reset statistics");
		b6.setFocusable(false);
		b6.setForeground(Color.WHITE);
		b6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				manager.resetStatistics();
				refreshGui();
			}
		});

		b7 = new JButton("Next");
		b7.setFocusable(false);
		b7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					manager.next();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,
							"An error has occcured.");
				}
				refreshGui();
			}
		});
		b8 = new JButton("Up");
		b8.setFocusable(false);
		b8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manager.voteUp();
				refreshGui();
			}
		});
		b9 = new JButton("Down");
		b9.setFocusable(false);
		b9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				manager.voteDown();
				refreshGui();
			}
		});
		b10 = new JButton("Skip");
		b10.setFocusable(false);
		b10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					manager.voteSkip();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,
							"An error has occcured.");
				}
				refreshGui();
			}
		});
		b4.setBackground(Color.DARK_GRAY);
		b5.setBackground(Color.DARK_GRAY);
		b6.setBackground(Color.DARK_GRAY);
		b7.setBackground(Color.GREEN);
		b8.setBackground(Color.GREEN);
		b9.setBackground(Color.RED);
		b10.setBackground(Color.GRAY);
		buttonPanel.add(b4);
		buttonPanel.add(b5);
		buttonPanel.add(b6);
		buttonPanel.add(b7);
		buttonPanel.add(b8);
		buttonPanel.add(b9);
		buttonPanel.add(b10);
		mainPanel.add(statisticsPanel, BorderLayout.NORTH);
		statisticsPanel.setLayout(new BorderLayout(0, 0));

		JPanel questionPanel = new JPanel();
		questionPanel.setBackground(Color.BLACK);
		questionPanel.setForeground(new Color(0, 0, 0));
		statisticsPanel.add(questionPanel, BorderLayout.WEST);

		JLabel label_1 = new JLabel("   ");
		label_1.setForeground(Color.LIGHT_GRAY);
		label_1.setBackground(Color.WHITE);
		questionPanel.add(label_1);

		JLabel lblQuestion = new JLabel("QUESTION:");
		lblQuestion.setForeground(Color.LIGHT_GRAY);
		questionPanel.add(lblQuestion);

		t8 = new JTextField();
		t8.setEnabled(false);
		t8.setEditable(false);
		t8.setForeground(Color.LIGHT_GRAY);
		t8.setBackground(Color.BLACK);
		questionPanel.add(t8);
		t8.setColumns(8);

		JLabel lblTotalQuestions = new JLabel("    TOTAL QUESTIONS");
		lblTotalQuestions.setForeground(Color.LIGHT_GRAY);
		questionPanel.add(lblTotalQuestions);

		t9 = new JTextField();
		t9.setEnabled(false);
		t9.setEditable(false);
		t9.setForeground(Color.LIGHT_GRAY);
		t9.setText("0");
		t9.setColumns(8);
		t9.setBackground(Color.BLACK);
		questionPanel.add(t9);

		JPanel statPanel = new JPanel();
		statPanel.setBackground(Color.BLACK);
		statisticsPanel.add(statPanel, BorderLayout.CENTER);

		// Generate labels
		JLabel l1 = new JLabel("COUNT:");
		l1.setForeground(Color.LIGHT_GRAY);
		statPanel.add(l1);

		// Generate text fields
		t1 = new JTextField("");
		t1.setEnabled(false);
		t1.setEditable(false);
		t1.setForeground(Color.LIGHT_GRAY);
		t1.setBackground(Color.BLACK);
		statPanel.add(t1);
		t1.setHorizontalAlignment(SwingConstants.RIGHT);

		// Column size
		t1.setColumns(6);
		JLabel l2 = new JLabel("    PERCENT:");
		l2.setForeground(Color.LIGHT_GRAY);
		statPanel.add(l2);
		t2 = new JTextField("%");
		t2.setEnabled(false);
		t2.setEditable(false);
		t2.setForeground(Color.LIGHT_GRAY);
		t2.setBackground(Color.BLACK);
		statPanel.add(t2);
		t2.setHorizontalAlignment(SwingConstants.RIGHT);
		t2.setColumns(6);
		JLabel l3 = new JLabel("    PASSED:");
		l3.setForeground(Color.LIGHT_GRAY);
		statPanel.add(l3);
		t3 = new JTextField("");
		t3.setEnabled(false);
		t3.setEditable(false);
		t3.setForeground(Color.LIGHT_GRAY);
		t3.setBackground(Color.BLACK);
		statPanel.add(t3);
		t3.setHorizontalAlignment(SwingConstants.RIGHT);
		t3.setColumns(6);
		JLabel l4 = new JLabel("    FAILED:");
		l4.setForeground(Color.LIGHT_GRAY);
		statPanel.add(l4);
		t4 = new JTextField("");
		t4.setEnabled(false);
		t4.setEditable(false);
		t4.setForeground(Color.LIGHT_GRAY);
		t4.setBackground(Color.BLACK);
		statPanel.add(t4);
		t4.setHorizontalAlignment(SwingConstants.RIGHT);
		t4.setColumns(6);
		JLabel l5 = new JLabel("    SKIPPED:");
		l5.setForeground(Color.LIGHT_GRAY);
		statPanel.add(l5);
		t5 = new JTextField("");
		t5.setEnabled(false);
		t5.setEditable(false);
		t5.setForeground(Color.LIGHT_GRAY);
		t5.setBackground(Color.BLACK);
		statPanel.add(t5);
		t5.setHorizontalAlignment(SwingConstants.RIGHT);
		t5.setColumns(6);

		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		statisticsPanel.add(panel, BorderLayout.EAST);

		JLabel label = new JLabel(
				"                        PREDICTION DISTANCE:");
		label.setForeground(Color.LIGHT_GRAY);
		panel.add(label);

		t10 = new JTextField("0");
		t10.setEnabled(false);
		t10.setEditable(false);
		t10.setForeground(Color.LIGHT_GRAY);
		t10.setHorizontalAlignment(SwingConstants.RIGHT);
		t10.setColumns(4);
		t10.setBackground(Color.BLACK);
		panel.add(t10);

		JLabel label_2 = new JLabel("    HISTORY RANGE:");
		label_2.setForeground(Color.LIGHT_GRAY);
		panel.add(label_2);

		t11 = new JTextField("0");
		t11.setEnabled(false);
		t11.setEditable(false);
		t11.setForeground(Color.LIGHT_GRAY);
		t11.setHorizontalAlignment(SwingConstants.RIGHT);
		t11.setColumns(4);
		t11.setBackground(Color.BLACK);
		panel.add(t11);

		JLabel label_3 = new JLabel("   ");
		label_3.setForeground(Color.LIGHT_GRAY);
		label_3.setBackground(Color.WHITE);
		panel.add(label_3);

		while (manager == null) {
			try {
				manager = new CmManager(chooseFile(), 24, 6);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"An error has occured, please retry the previous action\n"
								+ e.getMessage());
				manager = null;
			}
		}

		refreshGui();
		generateKeybindings();
		frame.setFocusable(true);
		frame.setVisible(true);
	}

	private void generateKeybindings() {
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					b8.doClick();
				}

				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					b9.doClick();
				}

				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					b10.doClick();
					b7.doClick();
				}

				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					b7.doClick();
				}

				if (e.getKeyCode() == KeyEvent.VK_F) {
					fullScreen = !fullScreen;
					if (fullScreen) {
						statisticsPanel.setVisible(false);
						buttonPanel.setVisible(false);
						mainPanel.revalidate();
						refreshGui();
					} else {
						statisticsPanel.setVisible(true);
						buttonPanel.setVisible(true);
						mainPanel.revalidate();
						refreshGui();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
	}

	private void updateChart() {
		chart = manager.getChart();
		chartPanel.setBackground(Color.BLACK);
		mainPanel.add(chartPanel, BorderLayout.CENTER);
		chartPanel.setChart(chart);
		chartPanel.setDisplayToolTips(false);
		BufferedImage image = new BufferedImage(16, 16,
				BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				image, new Point(0, 0), "blank cursor");
		chartPanel.setCursor(blankCursor);
		chartPanel.setVerticalAxisTrace(true);
		chartPanel.setHorizontalAxisTrace(true);
		chartPanel.setMouseZoomable(false);
		chartPanel.repaint();
	}

	private void refreshStatistics() {
		t1.setText(manager.getFailed() + manager.getPassed()
				+ manager.getSkipped() + "");
		t2.setText(String.format("%.2f\n", manager.getPercentPassed()) + "%");
		t3.setText(manager.getPassed() + "");
		t4.setText(manager.getFailed() + "");
		t5.setText(manager.getSkipped() + "");
		t8.setText(manager.getLocation() + "");
		t9.setText(manager.getTotalQuestions() + "");
		t10.setText(manager.getPredictionDistance() + "");
		t11.setText(manager.getHistoryRange() + "");
	}

	private void refreshGui() {
		if (manager.onQuestion()) {
			b7.setBackground(Color.WHITE);
			b7.setEnabled(false);
			b4.setVisible(true);
			b5.setVisible(true);
			b6.setVisible(true);
			b8.setVisible(true);
			b9.setVisible(true);
			b10.setVisible(true);
			b4.setEnabled(true);
			b5.setEnabled(true);
			b6.setEnabled(true);
			b8.setEnabled(true);
			b9.setEnabled(true);
			b10.setEnabled(true);
		} else {
			b7.setBackground(Color.GREEN);
			b7.setEnabled(true);
			b4.setVisible(false);
			b5.setVisible(false);
			b6.setVisible(false);
			b8.setVisible(false);
			b9.setVisible(false);
			b10.setVisible(false);
			b4.setEnabled(false);
			b5.setEnabled(false);
			b6.setEnabled(false);
			b8.setEnabled(false);
			b9.setEnabled(false);
			b10.setEnabled(false);
		}

		refreshStatistics();
		updateChart();
	}

	private File chooseFile() {
		JFileChooser chooser = new JFileChooser(".");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"CSV files", "csv");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else if (returnVal == JFileChooser.CANCEL_OPTION) {
			System.exit(0);
		}
		return null;
	}
}
