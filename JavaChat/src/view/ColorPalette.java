package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class ColorPalette extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7730812948472158293L;
	
	private final JColorChooser chooser;
	private ColorSwatch activeSwatch;
	
	public ColorPalette(JColorChooser cc) {
		this.chooser = cc;
		setLayout(new FlowLayout(FlowLayout.LEADING, 3, 3));
		//setSize(50,30);
		add(new ColorSwatch(Color.BLACK, false, true));
		add(new ColorSwatch(Color.GRAY, false, false));
		add(new ColorSwatch(Color.RED, false, false));
		add(new ColorSwatch(Color.PINK, false, false));
		add(new ColorSwatch(Color.YELLOW, false, false));
		add(new ColorSwatch(Color.WHITE, true, false));
	}
	
	private class ColorSwatch extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 3618934759783422187L;
		
		private final JDialog dialog;	
		
		private Color color;

		public ColorSwatch(Color c, final boolean displayColorChooser, final boolean initial) {
			this.color = c;
			
			if (displayColorChooser) {
				this.dialog = JColorChooser.createDialog(null, "Pick a color", false, chooser,
						new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								color = chooser.getColor();
								
								activeSwatch.setBorder(BorderFactory.createLineBorder(ColorPalette.this.getBackground(), 2));
								activeSwatch = ColorSwatch.this;
								setBorder(null);
								setBackground(color);
								repaint();
								return;
							}
						},
						new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {
								return;
							}
						});	
			} else {
				this.dialog = null;
			}
			
			setBackground(color);
			setSize(17, 17);
			setBorder(BorderFactory.createLineBorder(ColorPalette.this.getBackground(), 2));
			setPreferredSize(getSize());
			setMaximumSize(getSize());
			setMinimumSize(getSize());
			addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
					if (displayColorChooser) {
						dialog.setVisible(true);
					} else {
						chooser.setColor(color);
						activeSwatch.setBorder(BorderFactory.createLineBorder(ColorPalette.this.getBackground(), 2));
						activeSwatch = ColorSwatch.this;
						setBorder(null);
						setBackground(color);
						repaint();
					}
				}
			});
			
			if ( initial ) {
				activeSwatch = this;
				setBorder(null);
			}
		}
	}	
}
