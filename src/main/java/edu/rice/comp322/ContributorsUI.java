package edu.rice.comp322;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static edu.rice.hj.Module0.launchHabaneroApp;

/**
 * User-Interface for loading the contributors for all repositories
 * under the given organization.
 */
public class ContributorsUI extends JFrame implements LoadContributors {

    private final JTextField username = new JTextField(20);
    private final JPasswordField password = new JPasswordField(20);
    private final JTextField org = new JTextField(20);
    private final JButton load = new JButton("Load contributors");
    private final Insets insets = new Insets(3, 10, 3, 10);
    private final String[] columns = {"Login", "Contributions"};
    private final DefaultTableModel resultsModel = new DefaultTableModel(columns, 0);
    public List<User> users = new ArrayList<>();
    private final JTable results = new JTable(resultsModel);
    private final JScrollPane resultsScroll = new JScrollPane(results);
    private Preferences pref;

    public ContributorsUI() {
        init();
    }

    /**
     * Updates the contributors list displayed on the user-interface.
     * @param users a list of Users
     */
    public void updateContributors(List<User> users) {
        Object[][] values = new Object[users.size()][2];
        for (int i = 0; i < users.size(); i++) {
            values[i] = new Object[]{users.get(i).login, users.get(i).contributions};
        }
        this.users = users;
        resultsModel.setDataVector(values, columns);
    }

    /**
     * Adds action listener for load button.
     */
    private void addLoadListener() {
        load.addActionListener(e -> {
            String userParam = username.getText();
            String passParam = String.valueOf(password.getPassword());
            String orgParam = org.getText();
            if (!userParam.isEmpty() && !passParam.isEmpty()) {
                saveParams(userParam, passParam, orgParam);
            }
            new Thread(()-> {
                launchHabaneroApp(() -> {
                    try {
                        System.out.println("Loading Users ...");
                        loadContributorsPar(userParam, passParam, orgParam); //TODO change to use parallel implementation
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });
            }).start();
        });
    }

    /**
     * Sets the parameters.
     */
    public void setParams(String userParam, String passParam, String orgParam) {
        username.setText(userParam);
        password.setText(passParam);
        org.setText(orgParam);
    }

    /**
     * Saves the parameter data.
     */
    public void saveParams(String userParam, String passParam, String orgParam) {
        this.pref.put("username", userParam);
        this.pref.put("password", passParam);
        this.pref.put("org", orgParam);
    }

    /**
     * Initializes the user interface.
     */
    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 350);
        resultsScroll.setPreferredSize(new Dimension(200, 200));
        JPanel contentPane = new JPanel(new GridBagLayout());
        setContentPane(contentPane);
        addLabeled("GitHub Username", username);
        addLabeled("Password/Token", password);
        addWideSeparator();
        addLabeled("Organization", org);
        addWideSeparator();
        addLoad();
        addResultsScroll();
        this.pref = Preferences.userRoot().node("edu.rice.comp322.ContributorsUI");
        setParams(pref.get("username", ""), pref.get("password", ""), pref.get("org", ""));
        addLoadListener();
        updateContributors(new ArrayList<>());
    }


    /**
     * Adds labeled components.
     */
    private void addLabeled(String label, JComponent component) {
        GridBagConstraints constraintslbl =  new GridBagConstraints();
        constraintslbl.gridx = 0;
        constraintslbl.insets = insets;
        add(new JLabel(label), constraintslbl);
        GridBagConstraints constraintsComponent = new GridBagConstraints();
        constraintsComponent.gridx = 1;
        constraintsComponent.insets = insets;
        constraintsComponent.anchor = GridBagConstraints.WEST;
        constraintsComponent.fill = GridBagConstraints.HORIZONTAL;
        constraintsComponent.weightx = 1.0;
        add(component, constraintsComponent);
    }

    /**
     * Adds separator.
     */
    private void addWideSeparator() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.insets = insets;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(new JSeparator(), constraints);
    }

    /**
     * Adds load button.
     */
    private void addLoad() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.insets = insets;
        add(load, constraints);
    }

    /**
     * Add scrollbar for results.
     */
    private void addResultsScroll() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.insets = insets;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        add(resultsScroll, constraints);
    }

    public void start() {
        this.setVisible(true);
    }

}
