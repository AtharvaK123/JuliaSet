import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class JuliaSets extends JPanel implements AdjustmentListener,ActionListener
{
	JFrame frame;

	//int red = 0;
	//int green = 0;
	//int blue = 0;
	//JScrollBar redBar;
	//JScrollBar greenBar;
	//JScrollBar blueBar;
	JScrollBar zoomBar;
	JScrollBar ABar;
	JScrollBar BBar;
	JScrollBar hueBar;
	JScrollBar satBar;
	JScrollBar brightBar;
	JScrollBar shapeBar;

	JPanel scrollPanel;
	JPanel labelPanel;
	JPanel bigPanel;

	JButton resetButton, saveButton;

	//JLabel redLabel, greenLabel, blueLabel
	JLabel ALabel, BLabel, hueLabel, satLabel, zoomLabel, shapeLabel, brightLabel;
	Font font = new Font("Consolas", Font.PLAIN, 20);

	double zoom = 1;
	float maxIter = 300;
	double A, B, hue, sat, shape, bright;

	JFileChooser fileChooser;
	BufferedImage image;

	public JuliaSets()
	{
		frame = new JFrame("Julia Set Visualizer");
		frame.setSize(2100, 800);
		frame.add(this);

		String currDir=System.getProperty("user.dir");
		fileChooser=new JFileChooser(currDir);

		scrollPanel = new JPanel();
		labelPanel = new JPanel();
		bigPanel = new JPanel();

		//redBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 255);
		//greenBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 255);
		//blueBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 255);
		shapeBar = new JScrollBar(JScrollBar.HORIZONTAL, 10, 0, 0, 100);
		ABar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -2000, 2000);
		BBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -2000, 2000);
		hueBar = new JScrollBar(JScrollBar.HORIZONTAL, 661, 0, 0, 1000);
		satBar = new JScrollBar(JScrollBar.HORIZONTAL, 600, 0, 0, 1000);
		zoomBar = new JScrollBar(JScrollBar.HORIZONTAL, 10, 0, 0, 100);
		brightBar = new JScrollBar(JScrollBar.HORIZONTAL, 1, 0, 0, 100);

		resetButton = new JButton("Reset");
		resetButton.addActionListener(this);
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		A = ABar.getValue()/1000.0;
		B = BBar.getValue()/1000.0;
		hue = hueBar.getValue()/1000.0;
		sat = satBar.getValue()/1000.0;
		zoom = zoomBar.getValue()/10.0;
		shape = shapeBar.getValue()/10.0;
		bright = brightBar.getValue()/10.0;

		//scrollPanel.add(redBar);
		//scrollPanel.add(greenBar);
		//scrollPanel.add(blueBar);
		scrollPanel.add(shapeBar);
		scrollPanel.add(ABar);
		scrollPanel.add(BBar);
		scrollPanel.add(hueBar);
		scrollPanel.add(satBar);
		scrollPanel.add(zoomBar);
		scrollPanel.add(brightBar);
		scrollPanel.add(resetButton);
		scrollPanel.add(saveButton);


		//redBar.addAdjustmentListener(this);
		//greenBar.addAdjustmentListener(this);
		//blueBar.addAdjustmentListener(this);
		shapeBar.addAdjustmentListener(this);
		ABar.addAdjustmentListener(this);
		BBar.addAdjustmentListener(this);
		hueBar.addAdjustmentListener(this);
		satBar.addAdjustmentListener(this);
		zoomBar.addAdjustmentListener(this);
		brightBar.addAdjustmentListener(this);

		scrollPanel.setLayout(new GridLayout(9,1));
		labelPanel.setLayout(new GridLayout(9,1));
		bigPanel.setLayout(new BorderLayout());

		//redLabel = new JLabel("R: " + red);
		//redLabel.setFont(font);
		//redLabel.setPreferredSize(new Dimension(100, 20));
		//greenLabel = new JLabel("G: " + green);
		//greenLabel.setFont(font);
		//blueLabel = new JLabel("Bl: " + blue);
		//blueLabel.setFont(font);
		shapeLabel = new JLabel("Shape: " + shape);
		shapeLabel.setFont(font);
		ALabel = new JLabel("A: " + A);
		ALabel.setFont(font);
		BLabel = new JLabel("B: " + B);
		BLabel.setFont(font);
		hueLabel = new JLabel("Hue: " + hue);
		hueLabel.setFont(font);
		satLabel = new JLabel("Saturation: " + sat);
		satLabel.setFont(font);
		zoomLabel = new JLabel("Zoom: " + zoom);
		zoomLabel.setFont(font);
		brightLabel = new JLabel("Brightness: " + bright);
		brightLabel.setFont(font);

		//labelPanel.add(redLabel);
		//labelPanel.add(greenLabel);
		//labelPanel.add(blueLabel);
		labelPanel.add(shapeLabel);
		labelPanel.add(ALabel);
		labelPanel.add(BLabel);
		labelPanel.add(hueLabel);
		labelPanel.add(satLabel);
		labelPanel.add(zoomLabel);
		labelPanel.add(brightLabel);

		bigPanel.add(labelPanel, BorderLayout.WEST);
		bigPanel.add(scrollPanel, BorderLayout.CENTER);
		frame.add(bigPanel, BorderLayout.SOUTH);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//g.setColor(new Color(red, green, blue));
		//g.fillRect(0,0,this.getWidth(), this.getHeight());
		g.drawImage(drawJuliaSet(g), 0, 0, null);
	}

	public BufferedImage drawJuliaSet(Graphics g)
	{
		int width = this.getWidth();
		int height = this.getHeight();

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				double zx = 1.5*(i - width*0.5)/(0.5*zoom*width);
				double zy = (j - height*0.5)/(0.5*zoom*height);
				float iter = maxIter;

				while((zx*zx)+(zy*zy)<6 && iter > 0)
				{
					double temp = zx*zx - zy*zy + A;
					zy = shape*zx*zy + B;
					zx = temp;
					iter --;
				}

				int c;

				if(iter>0)
				{
					//outside color
					c = Color.HSBtoRGB((float)(iter/maxIter*hue), (float)sat, (float)bright);
				}
				else
				{
					//eye color
					c = Color.HSBtoRGB((float)hue, (float)sat, (float)bright);
				}
				image.setRGB(i, j, c);
			}
		}

		return image;
	}

	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		/*if(e.getSource() == redBar)
		{
			red = redBar.getValue();
			redLabel.setText("R:" + red);
		}
		if(e.getSource() == greenBar)
		{
			green = greenBar.getValue();
			greenLabel.setText("G:" + green);
		}
		if(e.getSource() == blueBar)
		{
			blue = blueBar.getValue();
			blueLabel.setText("Bl:" + blue);
		}*/
		if(e.getSource() == ABar)
		{
			A = ABar.getValue()/1000.0;
			ALabel.setText("A:" + A);
		}
		if(e.getSource() == BBar)
		{
			B = BBar.getValue()/1000.0;
			BLabel.setText("B:" + B);
		}
		if(e.getSource() == hueBar)
		{
			hue = hueBar.getValue()/1000.0;
			hueLabel.setText("Hue:" + hue);
		}
		if(e.getSource() == satBar)
		{
			sat = satBar.getValue()/1000.0;
			satLabel.setText("Saturation:" + sat);
		}
		if(e.getSource() == zoomBar)
		{
			zoom = zoomBar.getValue()/10.0;
			zoomLabel.setText("Zoom:" + zoom);
		}
		if(e.getSource() == shapeBar)
		{
			shape = shapeBar.getValue()/10.0;
			shapeLabel.setText("Shape:" + shape);
		}
		if(e.getSource() == brightBar)
		{
			bright = brightBar.getValue()/10.0;
			brightLabel.setText("Brightness:" + bright);
		}

		repaint();
	}
	public void reset()
	{
	//		redBar.setValue(0);
	//		blueBar.setValue(0);
	//		greenBar.setValue(0);
			ABar.setValue(0);
			BBar.setValue(0);
			hueBar.setValue(661);
			satBar.setValue(600);
			repaint();
	}


	public void actionPerformed(ActionEvent e)
	{

		if(e.getSource()==resetButton)
		{
			reset();
		}
		if(e.getSource()==saveButton)
		{
			saveImage();
		}
	}


	public void saveImage()
	{
		if(image!=null)
		{
			FileFilter filter=new FileNameExtensionFilter("*.png","png");
			fileChooser.setFileFilter(filter);
			if(fileChooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
			{
				File file=fileChooser.getSelectedFile();
				try
				{
					String st=file.getAbsolutePath();
					if(st.indexOf(".png")>=0)
						st=st.substring(0,st.length()-4);
					ImageIO.write(image,"png",new File(st+".png"));
				}catch(Exception e)
				{
				}

			}
		}
	}

	public static void main(String[]args)
	{
		JuliaSets app = new JuliaSets();
	}
}