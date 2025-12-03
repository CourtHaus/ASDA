package com.emotionalsurvey;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BarChartPanel extends JPanel {
	private int[] data;
	private static final int BAR_WIDTH = 25;
	private static final int SPACING = 10;
	private static final Color[] BAR_COLORS = { new Color(231, 76, 60), new Color(230, 126, 34),
			new Color(241, 196, 15), new Color(46, 204, 113), new Color(52, 152, 219) };

	public BarChartPanel() {
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(new Color(200, 200, 200), 1), "Response Distribution by Question", 0, 0,
				new Font("Arial", Font.BOLD, 14), new Color(60, 60, 60)), new EmptyBorder(10, 10, 10, 10)));
	}

	public void setData(int[] data) {
		this.data = data;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (data == null || data.length == 0) {
			return;
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int width = getWidth();
		int height = getHeight();
		int padding = 60;
		int chartHeight = height - 2 * padding;
		int chartWidth = width - 2 * padding;

		g2d.setColor(new Color(220, 220, 220));
		g2d.drawLine(padding, height - padding, width - padding, height - padding);
		g2d.drawLine(padding, padding, padding, height - padding);

		g2d.setColor(new Color(150, 150, 150));
		g2d.setFont(new Font("Arial", Font.PLAIN, 10));
		for (int i = 0; i <= 5; i++) {
			int y = height - padding - (i * chartHeight / 5);
			g2d.drawLine(padding - 5, y, padding, y);
			g2d.drawString(String.valueOf(i), padding - 25, y + 5);
		}

		int totalWidth = data.length * (BAR_WIDTH + SPACING);
		int startX = padding + (chartWidth - totalWidth) / 2;

		for (int i = 0; i < data.length; i++) {
			int barHeight = (data[i] * chartHeight) / 5;
			int x = startX + i * (BAR_WIDTH + SPACING);
			int y = height - padding - barHeight;

			Color barColor = BAR_COLORS[data[i] - 1];
			g2d.setColor(barColor);
			g2d.fillRect(x, y, BAR_WIDTH, barHeight);

			g2d.setColor(new Color(100, 100, 100));
			g2d.drawRect(x, y, BAR_WIDTH, barHeight);

			g2d.setFont(new Font("Arial", Font.PLAIN, 9));
			String label = String.valueOf(i + 1);
			int labelWidth = g2d.getFontMetrics().stringWidth(label);
			g2d.drawString(label, x + (BAR_WIDTH - labelWidth) / 2, height - padding + 15);
		}

		g2d.setColor(new Color(60, 60, 60));
		g2d.setFont(new Font("Arial", Font.BOLD, 12));
		String xLabel = "Question Number";
		int xLabelWidth = g2d.getFontMetrics().stringWidth(xLabel);
		g2d.drawString(xLabel, padding + (chartWidth - xLabelWidth) / 2, height - 15);

		g2d.rotate(-Math.PI / 2);
		String yLabel = "Response (1-5)";
		int yLabelWidth = g2d.getFontMetrics().stringWidth(yLabel);
		g2d.drawString(yLabel, -(height + yLabelWidth) / 2, 20);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(800, 400);
	}
}
