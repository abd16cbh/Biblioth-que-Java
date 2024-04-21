import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReturnBookWindow extends JFrame {
    private JComboBox<String> adherentsCombo;
    private JComboBox<String> livresEmpruntesCombo;
    private JButton retournerButton;
    private JLabel errorMessageLabel;

    public ReturnBookWindow() {
        super("Retourner un livre");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(15, 15, 15, 15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Sélectionner un adhérent:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        adherentsCombo = new JComboBox<>();
        loadAdherentsCombo();
        adherentsCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fillBorrowedBooksCombo();
            }
        });
        panel.add(adherentsCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Livres empruntés:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        livresEmpruntesCombo = new JComboBox<>();
        panel.add(livresEmpruntesCombo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        retournerButton = new JButton("Retourner");
        retournerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                retournerLivre();
            }
        });
        panel.add(retournerButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        errorMessageLabel = new JLabel("");
        errorMessageLabel.setForeground(Color.RED);

        panel.add(errorMessageLabel,gbc);

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Ajout du style
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(400, 200));
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));

        // Style pour les boutons
        retournerButton.setBackground(Color.GREEN);
        retournerButton.setForeground(Color.WHITE);

        add(panel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    // Méthode pour charger la liste des adhérents depuis la base de données
    private void loadAdherentsCombo() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/librairie-java-almabouada-abdennour", "root", "root");//Connexion à la BDD
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT nom, prenom FROM adherent");

            while (rs.next()) {
                adherentsCombo.addItem(rs.getString("nom") + " " + rs.getString("prenom"));
            }

            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Méthode pour remplir la liste des livres empruntés par un adhérent sélectionné
    private void fillBorrowedBooksCombo() {
        livresEmpruntesCombo.removeAllItems(); // Effacer les éléments précédents
        errorMessageLabel.setText(""); // Effacer le message d'erreur précédent
        String selectedAdherent = (String) adherentsCombo.getSelectedItem();
        if (selectedAdherent != null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost/librairie-java-almabouada-abdennour", "root", "root");//Connexion à la BDD
                PreparedStatement stmt = con.prepareStatement("SELECT livre.titre, emprunts.date_retour FROM emprunts JOIN livre ON emprunts.id_livre = livre.ISBN WHERE emprunts.id_adherent = (SELECT id_adherent FROM adherent WHERE nom = ? AND prenom = ?)");
                String[] nomPrenom = selectedAdherent.split(" ");
                stmt.setString(1, nomPrenom[0]);
                stmt.setString(2, nomPrenom[1]);
                ResultSet rs = stmt.executeQuery();

                boolean hasBorrowedBooks = false;
                while (rs.next()) {
                    hasBorrowedBooks = true;
                    String titreLivre = rs.getString("titre");
                    Date dateRetour = rs.getDate("date_retour");
                    int joursRestants = calculateRemainingDays(dateRetour);
                    livresEmpruntesCombo.addItem(titreLivre + " (Jours restants: " + joursRestants + ")");
                }

                if (!hasBorrowedBooks) {
                    errorMessageLabel.setText("Cet adhérent n'a pas emprunté de livre.");
                }

                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Méthode pour calculer le nombre de jours restants avant le retour d'un livre
   private int calculateRemainingDays(Date dateRetour) {
    LocalDate currentDate = LocalDate.now();
    LocalDate returnDate = dateRetour.toLocalDate();
    
    // Calculer la différence de jours entre la date de retour et la date actuelle
    long daysDifference = ChronoUnit.DAYS.between(currentDate, returnDate);
    
    // Assurer que le nombre de jours restants est positif
    int remainingDays = (int) Math.max(0, daysDifference);
    
    return remainingDays;
}


    // Méthode pour retourner un livre
    private void retournerLivre() {
        String selectedBook = (String) livresEmpruntesCombo.getSelectedItem();
        if (selectedBook != null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost/librairie-java-almabouada-abdennour", "root", "root");//Connexion à la BDD

                // Extraire le titre du livre à partir de l'élément de la liste déroulante
                String titreLivre = selectedBook.split(" ")[0];

                // Supprimer l'enregistrement de la table emprunt
                PreparedStatement deleteStmt = con.prepareStatement("DELETE FROM emprunts WHERE id_livre = (SELECT ISBN FROM livre WHERE titre = ?)");
                deleteStmt.setString(1, titreLivre);
                deleteStmt.executeUpdate();

                // Incrémenter le champ de disponibilité dans la table livre
                PreparedStatement updateStmt = con.prepareStatement("UPDATE livre SET disponibilite = disponibilite + 1 WHERE titre = ?");
                updateStmt.setString(1, titreLivre);
                updateStmt.executeUpdate();

JOptionPane.showMessageDialog(this, "<html><font color='green'>Livre retourné avec succès.</font></html>", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            errorMessageLabel.setText("Veuillez sélectionner un livre à retourner.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ReturnBookWindow();
            }
        });
    }
}
