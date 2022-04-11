package modelo.Presentacion;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Statement;

import modelo.Datos.ConexionDB;

public class VentanaLoad extends JFrame implements ChangeListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JProgressBar progreso;
	private JLabel lblEmpresa,lblPropietario,lblLogo;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Llamo conexion
				ConexionDB conexion = new ConexionDB();
				conexion.gConnection();
				//JOptionPane.showMessageDialog(null, "conexión realizada con éxito","SOFLE_HOTEL",JOptionPane.INFORMATION_MESSAGE); 
				try {
					VentanaLoad frame = new VentanaLoad();
					frame.setVisible(true);
					frame.setOpacity(0.8f);
				} catch (Exception e) {
					e.printStackTrace();
				}
				conexion.DesconectarDB();
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
    public void iniciarCarga() { //Utilizando Hilos
        new Thread() {
            @Override
            public void run() {
                int i = 0;
                while (i <= 100) {
                    i=i+3;
                    progreso.setValue(i);
                    try {
                        sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }
   void muestroDatosEmpresa(){
		ConexionDB conexion = new ConexionDB();
		conexion.gConnection();
		try {
			Statement statement = conexion.gConnection().createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM EMPRESA ");
			if (rs.next()==true) {
				lblEmpresa.setText(rs.getString("RazonSocial"));
				lblPropietario.setText(rs.getString("Propietario"));
				
			    Image i=null;
		        Blob blob = rs.getBlob("Logo");
		        i= javax.imageio.ImageIO.read(blob.getBinaryStream());
		        ImageIcon image = new ImageIcon(i);
				Icon icono = new ImageIcon(image.getImage().getScaledInstance(lblLogo.getWidth(), lblLogo.getHeight(), Image.SCALE_DEFAULT)); 
				lblLogo.setText(null);
				lblLogo.setIcon(icono);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
   }
   public void iniciarSplash() {
        this.getjProgressBar().setBorderPainted(true);
        this.getjProgressBar().setForeground(new Color(100, 0, 0, 50));
        this.getjProgressBar().setStringPainted(true);
    }

   public javax.swing.JProgressBar getjProgressBar() {
       return progreso;
    }
	
	public VentanaLoad() {
		progreso = new JProgressBar();
		progreso.setBounds(0, 234, 569, 17);
		progreso.addChangeListener(this);
		progreso.setVisible(false);
        try {
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//CAMBIA THEMA DE VENTANASDE TODO EL SIS
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setUndecorated(true);
        iniciarCarga();
        iniciarSplash();
        this.setSize(500, 300);
        this.setLocationRelativeTo(null);
        //this.pack();
        
        
		setType(Type.UTILITY);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 569, 250);
		setTitle("SISTEMA DE GESTION HOTELERO");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		contentPane.add(progreso);
		
		JLabel lblSistemaDeGestion = new JLabel("SISTEMA DE GESTION HOTELERO...");
		lblSistemaDeGestion.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSistemaDeGestion.setForeground(Color.ORANGE);
		lblSistemaDeGestion.setFont(new Font("Franklin Gothic Medium", Font.BOLD | Font.ITALIC, 17));
		lblSistemaDeGestion.setBounds(195, 11, 338, 14);
		contentPane.add(lblSistemaDeGestion);
		
		JLabel lblV = new JLabel("V.1.4.0 ");
		lblV.setHorizontalAlignment(SwingConstants.RIGHT);
		lblV.setForeground(Color.ORANGE);
		lblV.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 17));
		lblV.setBounds(439, 29, 94, 14);
		contentPane.add(lblV);
		
		lblEmpresa = new JLabel("nombre empresa");
		lblEmpresa.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEmpresa.setForeground(Color.ORANGE);
		lblEmpresa.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblEmpresa.setBounds(19, 60, 514, 17);
		contentPane.add(lblEmpresa);
		
		lblPropietario = new JLabel("proietario ");
		lblPropietario.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPropietario.setForeground(Color.ORANGE);
		lblPropietario.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 20));
		lblPropietario.setBounds(29, 82, 506, 17);
		contentPane.add(lblPropietario);
		
		JLabel lblCopyRight = new JLabel("SOFTLE:  Copy Right derechos reservados ");
		lblCopyRight.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCopyRight.setForeground(Color.ORANGE);
		lblCopyRight.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblCopyRight.setBounds(288, 218, 259, 17);
		contentPane.add(lblCopyRight);
		
		lblLogo = new JLabel("");
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogo.setBounds(364, 100, 171, 119);
		contentPane.add(lblLogo);
		lblLogo.setIcon(new ImageIcon(VentanaLoad.class.getResource("/modelo/Images/control32.png")));
		lblLogo.setVisible(false);
		
		JLabel lblFondo = new JLabel();
		lblFondo.setIcon(new ImageIcon(VentanaLoad.class.getResource("/modelo/Images/Load1.jpg")));
		lblFondo.setBounds(0, 0, 569, 251);
		contentPane.add(lblFondo);
		
		// FORMA REDONDA
		//setShape(new Ellipse2D.Double(0,0,getWidth(),getHeight()));//REDONDA
		
		setShape(new RoundRectangle2D.Double(0, 0, this.getBounds().width, this.getBounds().height, 100, 100));// OVALADA 
		muestroDatosEmpresa();
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
        if (progreso.getValue()== 100) {
			VentanaLogin ventana = new VentanaLogin();
			ventana.frame.setVisible(true);
			//ventana.opcacityLogin();
            this.dispose();
        }
	}

}
