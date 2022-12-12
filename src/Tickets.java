import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = null;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;

	public Tickets(Boolean isAdmin) {

		chkIfAdmin = isAdmin;
		createMenu();
		prepareGUI();

	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu items for Admin main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);

		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);


//		  * continue implementing any other desired sub menu items (like for update and delete sub menus for example) with similar syntax & logic as shown above

	}


	private void prepareGUI() {

		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		if (chkIfAdmin) {
			bar.add(mnuAdmin);
		}
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		setSize(400, 400);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// implement actions for sub menu items
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
		} else if (e.getSource() == mnuItemOpenTicket) {
			try {
				// tries to create a ticket, but if 'cancel' is clicked then it will throw an exception and will properly cancel the ticket creation
				String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
				if (ticketName == null) {
					throw new Exception();
				}

				while (ticketName != null && ("".equals(ticketName))) { //makes sure that the user enters a name for the ticket, if not it will keep prompting them until they do so or they click cancel
					ticketName = JOptionPane.showInputDialog(null, "Enter your name again");
					if (ticketName == null) {
						throw new Exception();
					}
				}

				String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
				if (ticketDesc == null) {
					throw new Exception();
				}

				while (ticketDesc != null && ("".equals(ticketDesc))) { //makes sure that the user enters a description for the ticket, if not it will keep prompting them until they do so or they click cancel
					ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description again");
					if (ticketDesc == null) {
						throw new Exception();
					}
				}

				// insert ticket information to database


				int id = dao.insertRecords(ticketName, ticketDesc);

				// display results if successful or not to console / dialog box
				if (id != 0) {
					System.out.println("Ticket ID : " + id + " created successfully!!!");
					JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");

				} else System.out.println("Ticket cannot be created!!!");
			} catch (Exception f) {
				JOptionPane.showMessageDialog(null, "cancelled");
			}


		} else if (e.getSource() == mnuItemViewTicket) {

			// retrieve all tickets details for viewing in JTable
			try {

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
				jt.setBounds(30, 40, 200, 400);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); // refreshes or repaints frame on screen

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == mnuItemDelete) {
			try {
				// tries to delete a ticket, but if 'cancel' is clicked then it will throw an exception and will properly cancel the ticket deletion
				String ticketId = JOptionPane.showInputDialog(null, "Enter the ticket id to delete");
				if (ticketId == null) {
					throw new Exception();
				}

				while (ticketId != null && ("".equals(ticketId))) { //makes sure that the user enters an id for the ticket, if not it will keep prompting them until they do so or they click cancel
					ticketId = JOptionPane.showInputDialog(null, "Enter the ticket id to delete again");
					if (ticketId == null) {
						throw new Exception();
					}
				}

				// delete ticket information from database
				int id = dao.deleteRecords(Integer.parseInt(ticketId));

				// display results if successful or not to console / dialog box
				if (id != 0) {
					System.out.println("Ticket ID : " + id + " deleted successfully!!!");
					JOptionPane.showMessageDialog(null, "Ticket id: " + id + " deleted");

				} else System.out.println("Ticket cannot be deleted!!!");
			} catch (Exception f) {
				JOptionPane.showMessageDialog(null, "cancelled");
			}
		} else if (e.getSource() == mnuItemUpdate) {
			try {
				// tries to update a ticket, but if 'cancel' is clicked then it will throw an exception and will properly cancel the ticket update
				String ticketId = JOptionPane.showInputDialog(null, "Enter the ticket id to update");
				if (ticketId == null) {
					throw new Exception();
				}

				while (ticketId != null && ("".equals(ticketId))) { //makes sure that the user enters an id for the ticket, if not it will keep prompting them until they do so or they click cancel
					ticketId = JOptionPane.showInputDialog(null, "Enter the ticket id to update again");
					if (ticketId == null) {
						throw new Exception();
					}
				}

				String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
				if (ticketName == null) {
					throw new Exception();
				}

				while (ticketName != null && ("".equals(ticketName))) { //makes sure that the user enters a name for the ticket, if not it will keep prompting them until they do so or they click cancel
					ticketName = JOptionPane.showInputDialog(null, "Enter your name again");
					if (ticketName == null) {
						throw new Exception();
					}
				}


				String ticketDesc = JOptionPane.showInputDialog(null, "Enter the new description");
				if (ticketDesc == null) {
					throw new Exception();
				}

				while (ticketDesc != null && ("".equals(ticketDesc))) { //makes sure that the user enters a name for the ticket, if not it will keep prompting them until they do so or they click cancel
					ticketDesc = JOptionPane.showInputDialog(null, "Enter the new description again");
					if (ticketDesc == null) {
						throw new Exception();
					}
				}
				//make one of the above sections of code but for the description

// update ticket information to database

				//cmon finish the code
// update ticket information to database
				int id = dao.updateRecords(Integer.parseInt(ticketId), ticketName, ticketDesc);

				// display results if successful or not to console / dialog box
				if (id != 0) {
					System.out.println("Ticket ID : " + id + " updated successfully!!!");
					JOptionPane.showMessageDialog(null, "Ticket id: " + id + " updated");

				} else System.out.println("Ticket cannot be updated!!!");
			} catch (Exception g) {
				JOptionPane.showMessageDialog(null, "cancelled");
			}
			/*
			 * continue implementing any other desired sub menu items (like for update and
			 * delete sub menus for example) with similar syntax & logic as shown above
			 */

		}

	}
}
