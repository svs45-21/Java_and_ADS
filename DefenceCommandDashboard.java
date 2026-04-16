import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class DefenceCommandDashboard extends JFrame {

    // ================== COLOR PALETTE ==================
    static final Color BG_DARK       = new Color(10, 12, 18);
    static final Color BG_PANEL      = new Color(16, 20, 30);
    static final Color BG_CARD       = new Color(22, 28, 42);
    static final Color BG_INPUT      = new Color(28, 35, 52);
    static final Color ACCENT_CYAN   = new Color(0, 210, 200);
    static final Color ACCENT_BLUE   = new Color(30, 120, 255);
    static final Color ACCENT_RED    = new Color(255, 60, 80);
    static final Color ACCENT_AMBER  = new Color(255, 180, 0);
    static final Color ACCENT_GREEN  = new Color(0, 220, 100);
    static final Color TEXT_PRIMARY  = new Color(220, 230, 255);
    static final Color TEXT_MUTED    = new Color(100, 115, 150);
    static final Color BORDER_COLOR  = new Color(40, 55, 85);

    // ================== DATA STRUCTURES ==================
    static class Node {
        String name;
        int x, y;
        String type; // "base","radar","drone","missile","target","custom"
        Node(String name, int x, int y, String type) {
            this.name = name; this.x = x; this.y = y; this.type = type;
        }
    }

    static class Edge {
        String target;
        int weight;
        Edge(String target, int weight) {
            this.target = target; this.weight = weight;
        }
    }

    private final Map<String, Node>        nodes = new LinkedHashMap<>();
    private final Map<String, List<Edge>>  graph = new LinkedHashMap<>();

    // ================== UI STATE ==================
    private List<String> currentPath     = new ArrayList<>();
    private int          animationIndex  = 0;
    private javax.swing.Timer animTimer;
    private String       hoveredNode     = null;
    private String       selectedFrom    = null;

    // ================== CONSOLE PANELS ==================
    private JPanel       addNodeConsole;
    private JPanel       connectConsole;
    private JPanel       autoConsole;
    private JPanel       activeConsole;
    private JTextArea    logArea;
    private GraphPanel   graphPanel;

    // ================== ADD NODE FIELDS ==================
    private JTextField   an_name, an_x, an_y;
    private JComboBox<String> an_type;

    // ================== CONNECT FIELDS ==================
    private JTextField   cn_from, cn_to, cn_weight;
    private JCheckBox    cn_directed;

    // ================== CONSTRUCTOR ==================
    public DefenceCommandDashboard() {
        setTitle("DEFENCE COMMAND — Navigation & Pathfinding System");
        setSize(1380, 820);
        setMinimumSize(new Dimension(1100, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BG_DARK);

        buildUI();
        setLocationRelativeTo(null);
    }

    // ================== UI BUILDER ==================
    private void buildUI() {
        // TOP BAR
        add(buildTopBar(), BorderLayout.NORTH);

        // CENTER: graph + right sidebar
        JPanel center = new JPanel(new BorderLayout(8, 0));
        center.setBackground(BG_DARK);
        center.setBorder(new EmptyBorder(0, 8, 8, 8));

        graphPanel = new GraphPanel();
        center.add(graphPanel, BorderLayout.CENTER);
        center.add(buildRightSidebar(), BorderLayout.EAST);

        // BOTTOM: consoles
        JPanel bottom = buildBottomSection();

        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    // ─────────── TOP BAR ───────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_PANEL);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(10, 18, 10, 18)
        ));

        // Title
        JLabel title = new JLabel("⬡  DEFENCE COMMAND DASHBOARD");
        title.setFont(new Font("Consolas", Font.BOLD, 16));
        title.setForeground(ACCENT_CYAN);

        // Status indicators
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        statusBar.setOpaque(false);
        statusBar.add(makePill("SYSTEM ONLINE", ACCENT_GREEN));
        statusBar.add(makePill("A* ENGINE ACTIVE", ACCENT_BLUE));
        statusBar.add(makePill("NODES: 0", ACCENT_AMBER));

        bar.add(title, BorderLayout.WEST);
        bar.add(statusBar, BorderLayout.EAST);
        return bar;
    }

    private JLabel makePill(String text, Color color) {
        JLabel l = new JLabel("● " + text);
        l.setFont(new Font("Consolas", Font.PLAIN, 11));
        l.setForeground(color);
        return l;
    }

    // ─────────── RIGHT SIDEBAR ───────────
    private JPanel buildRightSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BG_PANEL);
        sidebar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, BORDER_COLOR),
            new EmptyBorder(12, 12, 12, 12)
        ));

        sidebar.add(sectionHeader("SIMULATE PATH"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(buildSimPanel());
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(sectionHeader("NETWORK LEGEND"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(buildLegend());
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(sectionHeader("GRAPH STATS"));
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(buildStatsPanel());
        sidebar.add(Box.createGlue());

        return sidebar;
    }

    private JPanel buildSimPanel() {
        JPanel p = new JPanel(new GridLayout(4, 1, 4, 4));
        p.setOpaque(false);

        JTextField simFrom = styledField("Source node");
        JTextField simTo   = styledField("Target node");
        simFrom.setText("Base");
        simTo.setText("Target");

        JButton runBtn = accentButton("▶  RUN A* PATHFIND", ACCENT_CYAN);
        runBtn.addActionListener(e -> {
            String sf = simFrom.getText().trim();
            String st = simTo.getText().trim();
            if (!nodes.containsKey(sf) || !nodes.containsKey(st)) {
                log("[ERROR] Node not found: " + sf + " or " + st, ACCENT_RED);
                return;
            }
            List<String> path = aStar(sf, st);
            if (!path.isEmpty()) {
                log("[A*] Optimal path found: " + path, ACCENT_GREEN);
                log("[A*] Hops: " + (path.size()-1) + "  |  Initiating traversal...", ACCENT_CYAN);
                startAnimation(path);
            } else {
                log("[A*] No path found between " + sf + " and " + st, ACCENT_RED);
            }
        });

        JButton clearBtn = ghostButton("✕  CLEAR ANIMATION");
        clearBtn.addActionListener(e -> {
            if (animTimer != null) animTimer.stop();
            currentPath.clear();
            animationIndex = 0;
            graphPanel.repaint();
            log("[SIM] Animation cleared.", TEXT_MUTED);
        });

        p.add(simFrom);
        p.add(simTo);
        p.add(runBtn);
        p.add(clearBtn);
        return p;
    }

    private JPanel buildLegend() {
        JPanel p = new JPanel(new GridLayout(6, 1, 0, 3));
        p.setOpaque(false);
        p.add(legendItem(new Color(0, 200, 255), "Base"));
        p.add(legendItem(new Color(255, 200, 0), "Radar"));
        p.add(legendItem(new Color(0, 220, 100), "Drone"));
        p.add(legendItem(new Color(255, 100, 0), "Missile"));
        p.add(legendItem(new Color(255, 60, 80),  "Target"));
        p.add(legendItem(new Color(140, 100, 255), "Custom"));
        return p;
    }

    private JPanel legendItem(Color c, String label) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row.setOpaque(false);
        JPanel dot = new JPanel() {
            protected void paintComponent(Graphics g) {
                g.setColor(c);
                g.fillOval(0, 0, 10, 10);
            }
        };
        dot.setPreferredSize(new Dimension(10, 10));
        dot.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Consolas", Font.PLAIN, 12));
        lbl.setForeground(TEXT_MUTED);
        row.add(dot);
        row.add(lbl);
        return row;
    }

    private JLabel[] statLabels = new JLabel[4];

    private JPanel buildStatsPanel() {
        JPanel p = new JPanel(new GridLayout(4, 1, 2, 2));
        p.setOpaque(false);
        String[] keys = {"Nodes", "Edges", "Path Length", "Path Cost"};
        for (int i = 0; i < 4; i++) {
            JPanel row = new JPanel(new BorderLayout());
            row.setOpaque(false);
            JLabel k = new JLabel(keys[i]);
            k.setFont(new Font("Consolas", Font.PLAIN, 11));
            k.setForeground(TEXT_MUTED);
            statLabels[i] = new JLabel("—");
            statLabels[i].setFont(new Font("Consolas", Font.BOLD, 12));
            statLabels[i].setForeground(ACCENT_CYAN);
            statLabels[i].setHorizontalAlignment(SwingConstants.RIGHT);
            row.add(k, BorderLayout.WEST);
            row.add(statLabels[i], BorderLayout.EAST);
            p.add(row);
        }
        return p;
    }

    // ─────────── BOTTOM SECTION: CONSOLES + LOG ───────────
    private JPanel buildBottomSection() {
        JPanel bottom = new JPanel(new BorderLayout(8, 0));
        bottom.setBackground(BG_DARK);
        bottom.setBorder(new EmptyBorder(0, 8, 8, 8));
        bottom.setPreferredSize(new Dimension(0, 240));

        // Console tabs on left
        JPanel consoleArea = buildConsoleArea();
        bottom.add(consoleArea, BorderLayout.CENTER);

        // Log on right
        bottom.add(buildLogPanel(), BorderLayout.EAST);

        return bottom;
    }

    private JPanel buildConsoleArea() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 6));
        wrapper.setBackground(BG_DARK);

        // Tab bar
        JPanel tabBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        tabBar.setBackground(BG_DARK);

        String[] tabs = {"ADD NODE", "CONNECT NODES", "AUTO NETWORK"};
        JButton[] tabBtns = new JButton[tabs.length];

        // Consoles
        JPanel consoleStack = new JPanel(new CardLayout());
        consoleStack.setBackground(BG_CARD);

        addNodeConsole = buildAddNodeConsole();
        connectConsole = buildConnectConsole();
        autoConsole    = buildAutoConsole();

        consoleStack.add(addNodeConsole, "ADD NODE");
        consoleStack.add(connectConsole, "CONNECT NODES");
        consoleStack.add(autoConsole,    "AUTO NETWORK");

        CardLayout cl = (CardLayout) consoleStack.getLayout();

        for (int i = 0; i < tabs.length; i++) {
            final String tabName = tabs[i];
            tabBtns[i] = consoleTabButton(tabName, i == 0);
            tabBtns[i].addActionListener(e -> {
                cl.show(consoleStack, tabName);
                for (JButton b : tabBtns) b.setBackground(BG_CARD);
                ((JButton)e.getSource()).setBackground(BG_INPUT);
            });
            tabBar.add(tabBtns[i]);
        }

        wrapper.add(tabBar, BorderLayout.NORTH);
        wrapper.add(consoleStack, BorderLayout.CENTER);
        return wrapper;
    }

    // ─── ADD NODE CONSOLE ───
    private JPanel buildAddNodeConsole() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG_CARD);
        p.setBorder(new EmptyBorder(12, 14, 12, 14));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(4, 6, 4, 6);

        an_name = styledField("e.g. Radar_East");
        an_x    = styledField("X coordinate (auto if empty)");
        an_y    = styledField("Y coordinate (auto if empty)");
        an_type = styledCombo(new String[]{"custom","base","radar","drone","missile","target"});

        gc.gridy = 0; gc.gridx = 0; gc.weightx = 0.15;
        p.add(consoleLabel("NODE NAME"), gc);
        gc.gridx = 1; gc.weightx = 0.35;
        p.add(an_name, gc);

        gc.gridx = 2; gc.weightx = 0.1;
        p.add(consoleLabel("TYPE"), gc);
        gc.gridx = 3; gc.weightx = 0.15;
        p.add(an_type, gc);

        gc.gridx = 4; gc.weightx = 0.1;
        p.add(consoleLabel("X POS"), gc);
        gc.gridx = 5; gc.weightx = 0.1;
        p.add(an_x, gc);

        gc.gridx = 6; gc.weightx = 0.05;
        p.add(consoleLabel("Y POS"), gc);
        gc.gridx = 7; gc.weightx = 0.1;
        p.add(an_y, gc);

        // Add button
        JButton addBtn = accentButton("+ ADD NODE", ACCENT_GREEN);
        addBtn.addActionListener(e -> doAddNode());
        gc.gridy = 1; gc.gridx = 0; gc.gridwidth = 2;
        p.add(addBtn, gc);

        // Hint
        gc.gridx = 2; gc.gridwidth = 6;
        JLabel hint = new JLabel("Leave X/Y empty for random placement. Node name must be unique.");
        hint.setFont(new Font("Consolas", Font.ITALIC, 10));
        hint.setForeground(TEXT_MUTED);
        p.add(hint, gc);

        return p;
    }

    private void doAddNode() {
        String name = an_name.getText().trim();
        if (name.isEmpty()) { log("[ERROR] Node name cannot be empty.", ACCENT_RED); return; }
        if (nodes.containsKey(name)) { log("[ERROR] Node '" + name + "' already exists.", ACCENT_RED); return; }

        int x, y;
        try {
            x = an_x.getText().trim().isEmpty() ? (new Random().nextInt(720) + 60)
                                                 : Integer.parseInt(an_x.getText().trim());
            y = an_y.getText().trim().isEmpty() ? (new Random().nextInt(380) + 60)
                                                 : Integer.parseInt(an_y.getText().trim());
        } catch (NumberFormatException ex) {
            log("[ERROR] X/Y must be integers.", ACCENT_RED); return;
        }

        String type = (String) an_type.getSelectedItem();
        addNode(name, x, y, type);
        log("[NODE] Added '" + name + "' at (" + x + "," + y + ") — Type: " + type, ACCENT_GREEN);
        updateStats();
        graphPanel.repaint();
        an_name.setText(""); an_x.setText(""); an_y.setText("");
    }

    // ─── CONNECT CONSOLE ───
    private JPanel buildConnectConsole() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG_CARD);
        p.setBorder(new EmptyBorder(12, 14, 12, 14));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(4, 6, 4, 6);

        cn_from     = styledField("Source node name");
        cn_to       = styledField("Destination node name");
        cn_weight   = styledField("Weight / cost");
        cn_directed = new JCheckBox("Directed (one-way)");
        cn_directed.setFont(new Font("Consolas", Font.PLAIN, 11));
        cn_directed.setForeground(TEXT_MUTED);
        cn_directed.setBackground(BG_CARD);
        cn_directed.setFocusPainted(false);

        gc.gridy = 0; gc.gridx = 0; gc.weightx = 0.12;
        p.add(consoleLabel("FROM"), gc);
        gc.gridx = 1; gc.weightx = 0.28;
        p.add(cn_from, gc);

        gc.gridx = 2; gc.weightx = 0.08;
        p.add(consoleLabel("TO"), gc);
        gc.gridx = 3; gc.weightx = 0.28;
        p.add(cn_to, gc);

        gc.gridx = 4; gc.weightx = 0.1;
        p.add(consoleLabel("WEIGHT"), gc);
        gc.gridx = 5; gc.weightx = 0.14;
        p.add(cn_weight, gc);

        gc.gridy = 1; gc.gridx = 0; gc.gridwidth = 2;
        JButton connBtn = accentButton("⟶ CONNECT", ACCENT_BLUE);
        connBtn.addActionListener(e -> doConnect());
        p.add(connBtn, gc);

        gc.gridx = 2; gc.gridwidth = 2;
        p.add(cn_directed, gc);

        gc.gridx = 4; gc.gridwidth = 2;
        JButton removeBtn = ghostButton("✕ REMOVE EDGE");
        removeBtn.addActionListener(e -> doRemoveEdge());
        p.add(removeBtn, gc);

        return p;
    }

    private void doConnect() {
        String from   = cn_from.getText().trim();
        String to     = cn_to.getText().trim();
        String wStr   = cn_weight.getText().trim();
        if (from.isEmpty() || to.isEmpty() || wStr.isEmpty()) {
            log("[ERROR] All fields (From, To, Weight) are required.", ACCENT_RED); return;
        }
        if (!nodes.containsKey(from)) { log("[ERROR] Node not found: " + from, ACCENT_RED); return; }
        if (!nodes.containsKey(to))   { log("[ERROR] Node not found: " + to,   ACCENT_RED); return; }
        int weight;
        try { weight = Integer.parseInt(wStr); }
        catch (NumberFormatException ex) { log("[ERROR] Weight must be an integer.", ACCENT_RED); return; }

        boolean directed = cn_directed.isSelected();
        graph.get(from).add(new Edge(to, weight));
        if (!directed) graph.get(to).add(new Edge(from, weight));
        log("[EDGE] " + from + (directed ? " ──► " : " ◄──► ") + to + "  weight=" + weight, ACCENT_BLUE);
        updateStats();
        graphPanel.repaint();
        cn_from.setText(""); cn_to.setText(""); cn_weight.setText("");
    }

    private void doRemoveEdge() {
        String from = cn_from.getText().trim();
        String to   = cn_to.getText().trim();
        if (!nodes.containsKey(from)) { log("[ERROR] Node not found: " + from, ACCENT_RED); return; }
        graph.get(from).removeIf(e -> e.target.equals(to));
        graph.get(to).removeIf(e -> e.target.equals(from));
        log("[EDGE] Removed edge between " + from + " and " + to, ACCENT_AMBER);
        updateStats();
        graphPanel.repaint();
    }

    // ─── AUTO NETWORK CONSOLE ───
    private JPanel buildAutoConsole() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG_CARD);
        p.setBorder(new EmptyBorder(12, 14, 12, 14));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(4, 8, 4, 8);

        JLabel desc = new JLabel("<html>Load a pre-configured defence network topology. "
            + "Choose a scenario below and click <b>Load</b>.</html>");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        desc.setForeground(TEXT_MUTED);
        gc.gridy = 0; gc.gridx = 0; gc.gridwidth = 5; gc.weightx = 1;
        p.add(desc, gc);

        JComboBox<String> scenarioBox = styledCombo(new String[]{
            "Standard (5 nodes)", "Extended Grid (8 nodes)", "Ring Topology (6 nodes)"
        });
        gc.gridy = 1; gc.gridwidth = 3; gc.weightx = 0.5;
        p.add(scenarioBox, gc);

        JButton loadBtn = accentButton("⟳ LOAD NETWORK", ACCENT_AMBER);
        loadBtn.addActionListener(e -> {
            int idx = scenarioBox.getSelectedIndex();
            if (idx == 0) loadStandardNetwork();
            else if (idx == 1) loadExtendedNetwork();
            else loadRingNetwork();
        });
        gc.gridx = 3; gc.gridwidth = 2; gc.weightx = 0.3;
        p.add(loadBtn, gc);

        JButton clearBtn = ghostButton("✕ CLEAR ALL");
        clearBtn.addActionListener(e -> {
            if (animTimer != null) animTimer.stop();
            nodes.clear(); graph.clear();
            currentPath.clear(); animationIndex = 0;
            log("[NET] Network cleared.", ACCENT_AMBER);
            updateStats(); graphPanel.repaint();
        });
        gc.gridx = 0; gc.gridy = 2; gc.gridwidth = 2; gc.weightx = 0.3;
        p.add(clearBtn, gc);

        return p;
    }

    // ─── LOG PANEL ───
    private JPanel buildLogPanel() {
        JPanel lp = new JPanel(new BorderLayout(0, 4));
        lp.setPreferredSize(new Dimension(360, 0));
        lp.setBackground(BG_PANEL);
        lp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, BORDER_COLOR),
            new EmptyBorder(8, 10, 8, 10)
        ));

        JLabel logHeader = new JLabel("SYSTEM LOG");
        logHeader.setFont(new Font("Consolas", Font.BOLD, 11));
        logHeader.setForeground(TEXT_MUTED);

        logArea = new JTextArea();
        logArea.setBackground(BG_DARK);
        logArea.setForeground(ACCENT_GREEN);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        logArea.setEditable(false);
        logArea.setBorder(new EmptyBorder(6, 8, 6, 8));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scroll.getViewport().setBackground(BG_DARK);

        JButton clearLog = ghostButton("Clear");
        clearLog.setFont(new Font("Consolas", Font.PLAIN, 10));
        clearLog.addActionListener(e -> logArea.setText(""));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(logHeader, BorderLayout.WEST);
        header.add(clearLog, BorderLayout.EAST);

        lp.add(header, BorderLayout.NORTH);
        lp.add(scroll, BorderLayout.CENTER);
        return lp;
    }

    // ================== GRAPH PANEL ==================
    class GraphPanel extends JPanel {
        GraphPanel() {
            setBackground(BG_DARK);
            setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
            setToolTipText("");

            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseMoved(MouseEvent e) {
                    String prev = hoveredNode;
                    hoveredNode = null;
                    for (Node n : nodes.values()) {
                        if (dist(e.getX(), e.getY(), n.x, n.y) < 20) {
                            hoveredNode = n.name;
                            break;
                        }
                    }
                    if (!Objects.equals(prev, hoveredNode)) repaint();
                }
            });

            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    for (Node n : nodes.values()) {
                        if (dist(e.getX(), e.getY(), n.x, n.y) < 20) {
                            if (selectedFrom == null) {
                                selectedFrom = n.name;
                                cn_from.setText(n.name);
                                log("[SELECT] From: " + n.name + " — now click To node or type in console.", ACCENT_CYAN);
                            } else {
                                cn_to.setText(n.name);
                                selectedFrom = null;
                            }
                            repaint();
                            return;
                        }
                    }
                }
            });
        }

        protected void paintComponent(Graphics g2d) {
            super.paintComponent(g2d);
            Graphics2D g = (Graphics2D) g2d;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Grid dots
            g.setColor(new Color(30, 38, 58));
            for (int x = 0; x < getWidth(); x += 30)
                for (int y = 0; y < getHeight(); y += 30)
                    g.fillOval(x-1, y-1, 2, 2);

            // Draw edges
            Set<String> pathEdges = buildPathEdgeSet();
            for (String u : graph.keySet()) {
                Node n1 = nodes.get(u);
                if (n1 == null) continue;
                for (Edge e : graph.get(u)) {
                    Node n2 = nodes.get(e.target);
                    if (n2 == null) continue;

                    boolean onPath = pathEdges.contains(u + ">" + e.target)
                                  || pathEdges.contains(e.target + ">" + u);

                    if (onPath) {
                        g.setColor(ACCENT_CYAN);
                        g.setStroke(new BasicStroke(2.5f));
                    } else {
                        g.setColor(new Color(55, 70, 110));
                        g.setStroke(new BasicStroke(1.2f));
                    }

                    g.drawLine(n1.x, n1.y, n2.x, n2.y);

                    // Weight label
                    int mx = (n1.x + n2.x)/2, my = (n1.y + n2.y)/2;
                    g.setColor(BG_DARK);
                    g.fillRoundRect(mx-10, my-9, 20, 14, 4, 4);
                    g.setColor(onPath ? ACCENT_CYAN : TEXT_MUTED);
                    g.setFont(new Font("Consolas", Font.BOLD, 10));
                    g.drawString(String.valueOf(e.weight), mx-5, my+1);
                }
            }
            g.setStroke(new BasicStroke(1.5f));

            // Draw path trail
            if (currentPath.size() > 1) {
                for (int i = 0; i < Math.min(animationIndex + 1, currentPath.size() - 1); i++) {
                    Node a = nodes.get(currentPath.get(i));
                    Node b = nodes.get(currentPath.get(i+1));
                    if (a == null || b == null) continue;
                    g.setColor(new Color(0, 210, 200, 80));
                    g.setStroke(new BasicStroke(8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.drawLine(a.x, a.y, b.x, b.y);
                    g.setStroke(new BasicStroke(1.5f));
                }
            }

            // Draw nodes
            for (Node n : nodes.values()) {
                boolean hovered  = n.name.equals(hoveredNode);
                boolean selected = n.name.equals(selectedFrom);
                boolean onPath   = currentPath.contains(n.name);

                Color nc = nodeColor(n.type);
                int r = hovered ? 22 : 18;

                // Glow ring
                if (onPath || selected) {
                    Color glow = selected ? ACCENT_AMBER : ACCENT_CYAN;
                    g.setColor(new Color(glow.getRed(), glow.getGreen(), glow.getBlue(), 40));
                    g.fillOval(n.x - r - 6, n.y - r - 6, (r+6)*2, (r+6)*2);
                }

                // Node fill
                g.setColor(BG_CARD);
                g.fillOval(n.x - r, n.y - r, r*2, r*2);
                g.setColor(nc);
                g.setStroke(new BasicStroke(hovered ? 2.5f : 1.8f));
                g.drawOval(n.x - r, n.y - r, r*2, r*2);

                // Inner dot
                g.fillOval(n.x - 4, n.y - 4, 8, 8);

                // Label
                g.setFont(new Font("Consolas", Font.BOLD, 11));
                FontMetrics fm = g.getFontMetrics();
                int tw = fm.stringWidth(n.name);
                g.setColor(BG_DARK);
                g.fillRoundRect(n.x - tw/2 - 3, n.y - r - 16, tw + 6, 13, 3, 3);
                g.setColor(hovered ? Color.WHITE : TEXT_PRIMARY);
                g.drawString(n.name, n.x - tw/2, n.y - r - 5);
            }

            // Missile animation
            if (!currentPath.isEmpty() && animationIndex < currentPath.size()) {
                Node cur = nodes.get(currentPath.get(animationIndex));
                if (cur != null) {
                    // Pulse rings
                    for (int ring = 0; ring < 3; ring++) {
                        int rr = 14 + ring * 7;
                        g.setColor(new Color(255, 60, 80, 60 - ring * 18));
                        g.fillOval(cur.x - rr, cur.y - rr, rr*2, rr*2);
                    }
                    g.setColor(ACCENT_RED);
                    g.fillOval(cur.x - 10, cur.y - 10, 20, 20);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Consolas", Font.BOLD, 9));
                    g.drawString("●", cur.x - 3, cur.y + 3);
                }
            }

            // Empty state
            if (nodes.isEmpty()) {
                g.setFont(new Font("Consolas", Font.PLAIN, 14));
                g.setColor(new Color(50, 65, 95));
                String msg = "No nodes — use the consoles below to add nodes or load a network";
                g.drawString(msg, getWidth()/2 - g.getFontMetrics().stringWidth(msg)/2, getHeight()/2);
            }
        }

        private Set<String> buildPathEdgeSet() {
            Set<String> s = new HashSet<>();
            for (int i = 0; i < currentPath.size() - 1; i++)
                s.add(currentPath.get(i) + ">" + currentPath.get(i+1));
            return s;
        }

        private double dist(int x1, int y1, int x2, int y2) {
            return Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
        }

        private Color nodeColor(String type) {
            return switch (type) {
                case "base"    -> new Color(0, 200, 255);
                case "radar"   -> new Color(255, 200, 0);
                case "drone"   -> new Color(0, 220, 100);
                case "missile" -> new Color(255, 100, 0);
                case "target"  -> new Color(255, 60, 80);
                default        -> new Color(140, 100, 255);
            };
        }
    }

    // ================== NETWORK LOADERS ==================
    private void loadStandardNetwork() {
        clearNetwork();
        int[] xs = {100, 280, 460, 660, 860};
        int[] ys = {300, 140, 220, 380, 300};
        String[][] nodeData = {{"Base","base"},{"Radar","radar"},{"Drone","drone"},{"Missile","missile"},{"Target","target"}};
        for (int i = 0; i < nodeData.length; i++)
            addNode(nodeData[i][0], xs[i], ys[i], nodeData[i][1]);
        int[][] edges = {{0,1,4},{1,2,3},{2,3,5},{3,4,2},{0,2,7},{1,3,6}};
        String[] nm = {"Base","Radar","Drone","Missile","Target"};
        for (int[] ed : edges) {
            graph.get(nm[ed[0]]).add(new Edge(nm[ed[1]], ed[2]));
            graph.get(nm[ed[1]]).add(new Edge(nm[ed[0]], ed[2]));
        }
        log("[NET] Standard Defence Network loaded (5 nodes, 6 edges).", ACCENT_AMBER);
        updateStats(); graphPanel.repaint();
    }

    private void loadExtendedNetwork() {
        clearNetwork();
        String[][] nd = {
            {"HQ","base","80","300"},{"Sat1","radar","220","100"},{"Sat2","radar","380","80"},
            {"Drone_A","drone","300","260"},{"Drone_B","drone","500","200"},
            {"Launcher","missile","620","340"},{"Shield","missile","460","420"},{"Target","target","780","300"}
        };
        for (String[] d : nd) addNode(d[0], Integer.parseInt(d[2]), Integer.parseInt(d[3]), d[1]);
        int[][] edges = {
            {0,1,5},{0,3,6},{1,2,3},{1,3,4},{2,4,5},{3,4,3},{3,6,7},{4,5,4},{4,7,9},{5,7,3},{6,5,4}
        };
        String[] names = {"HQ","Sat1","Sat2","Drone_A","Drone_B","Launcher","Shield","Target"};
        for (int[] ed : edges) {
            graph.get(names[ed[0]]).add(new Edge(names[ed[1]], ed[2]));
            graph.get(names[ed[1]]).add(new Edge(names[ed[0]], ed[2]));
        }
        log("[NET] Extended Grid Network loaded (8 nodes, 11 edges).", ACCENT_AMBER);
        updateStats(); graphPanel.repaint();
    }

    private void loadRingNetwork() {
        clearNetwork();
        String[][] nd = {
            {"Alpha","base","400","80"},{"Bravo","radar","620","220"},{"Charlie","drone","580","420"},
            {"Delta","missile","380","480"},{"Echo","radar","170","380"},{"Foxtr","drone","150","190"}
        };
        for (String[] d : nd) addNode(d[0], Integer.parseInt(d[2]), Integer.parseInt(d[3]), d[1]);
        String[] nm = {"Alpha","Bravo","Charlie","Delta","Echo","Foxtr"};
        for (int i = 0; i < nm.length; i++) {
            String a = nm[i], b = nm[(i+1)%nm.length];
            graph.get(a).add(new Edge(b, 3 + i));
            graph.get(b).add(new Edge(a, 3 + i));
        }
        // Center cross
        graph.get("Alpha").add(new Edge("Delta", 8));
        graph.get("Delta").add(new Edge("Alpha", 8));
        graph.get("Bravo").add(new Edge("Echo", 10));
        graph.get("Echo").add(new Edge("Bravo", 10));
        log("[NET] Ring Topology Network loaded (6 nodes).", ACCENT_AMBER);
        updateStats(); graphPanel.repaint();
    }

    private void clearNetwork() {
        if (animTimer != null) animTimer.stop();
        nodes.clear(); graph.clear();
        currentPath.clear(); animationIndex = 0;
    }

    // ================== GRAPH HELPERS ==================
    private void addNode(String name, int x, int y, String type) {
        nodes.put(name, new Node(name, x, y, type));
        graph.put(name, new ArrayList<>());
    }

    private void updateStats() {
        int edgeCount = 0;
        for (List<Edge> el : graph.values()) edgeCount += el.size();
        statLabels[0].setText(String.valueOf(nodes.size()));
        statLabels[1].setText(String.valueOf(edgeCount / 2));
        if (!currentPath.isEmpty()) {
            statLabels[2].setText(String.valueOf(currentPath.size() - 1) + " hops");
            // compute cost
            int cost = 0;
            for (int i = 0; i < currentPath.size() - 1; i++) {
                String a = currentPath.get(i), b = currentPath.get(i+1);
                for (Edge e : graph.get(a)) if (e.target.equals(b)) { cost += e.weight; break; }
            }
            statLabels[3].setText(String.valueOf(cost));
        } else {
            statLabels[2].setText("—"); statLabels[3].setText("—");
        }
    }

    // ================== A* ==================
    private double heuristic(Node a, Node b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public List<String> aStar(String start, String goal) {
        Map<String, Double> gScore = new HashMap<>();
        Map<String, Double> fScore = new HashMap<>();
        Map<String, String> cameFrom = new HashMap<>();
        for (String n : nodes.keySet()) { gScore.put(n, Double.MAX_VALUE); fScore.put(n, Double.MAX_VALUE); }
        gScore.put(start, 0.0);
        fScore.put(start, heuristic(nodes.get(start), nodes.get(goal)));
        PriorityQueue<String> open = new PriorityQueue<>(Comparator.comparingDouble(fScore::get));
        open.add(start);
        while (!open.isEmpty()) {
            String curr = open.poll();
            if (curr.equals(goal)) {
                List<String> path = new ArrayList<>();
                while (curr != null) { path.add(0, curr); curr = cameFrom.get(curr); }
                return path;
            }
            for (Edge e : graph.get(curr)) {
                double t = gScore.get(curr) + e.weight;
                if (t < gScore.get(e.target)) {
                    cameFrom.put(e.target, curr);
                    gScore.put(e.target, t);
                    fScore.put(e.target, t + heuristic(nodes.get(e.target), nodes.get(goal)));
                    if (!open.contains(e.target)) open.add(e.target);
                }
            }
        }
        return new ArrayList<>();
    }

    // ================== ANIMATION ==================
    private void startAnimation(List<String> path) {
        if (animTimer != null) animTimer.stop();
        currentPath = path;
        animationIndex = 0;
        updateStats();
        animTimer = new javax.swing.Timer(650, e -> {
            if (animationIndex < currentPath.size() - 1) {
                animationIndex++;
                graphPanel.repaint();
            } else {
                ((javax.swing.Timer)e.getSource()).stop();
                log("[SIM] ✓ TARGET REACHED — Mission complete.", ACCENT_GREEN);
                updateStats();
            }
        });
        animTimer.start();
    }

    // ================== LOG ==================
    private void log(String msg, Color color) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    // ================== UI HELPERS ==================
    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    g.setColor(TEXT_MUTED);
                    g.setFont(getFont().deriveFont(Font.ITALIC));
                    g.drawString(placeholder, 6, g.getFontMetrics().getAscent() + 3);
                }
            }
        };
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(ACCENT_CYAN);
        f.setFont(new Font("Consolas", Font.PLAIN, 12));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(4, 6, 4, 6)
        ));
        return f;
    }

    private <T> JComboBox<T> styledCombo(T[] items) {
        JComboBox<T> cb = new JComboBox<>(items);
        cb.setBackground(BG_INPUT);
        cb.setForeground(TEXT_PRIMARY);
        cb.setFont(new Font("Consolas", Font.PLAIN, 12));
        cb.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        return cb;
    }

    private JButton accentButton(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
        b.setForeground(color);
        b.setFont(new Font("Consolas", Font.BOLD, 11));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(color.getRed(), color.getGreen(), color.getBlue(), 120), 1),
            new EmptyBorder(6, 12, 6, 12)
        ));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 70));
            }
            public void mouseExited(MouseEvent e) {
                b.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
            }
        });
        return b;
    }

    private JButton ghostButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(BG_CARD);
        b.setForeground(TEXT_MUTED);
        b.setFont(new Font("Consolas", Font.PLAIN, 11));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton consoleTabButton(String text, boolean active) {
        JButton b = new JButton(text);
        b.setBackground(active ? BG_INPUT : BG_CARD);
        b.setForeground(active ? ACCENT_CYAN : TEXT_MUTED);
        b.setFont(new Font("Consolas", Font.BOLD, 11));
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 0, 1, BORDER_COLOR),
            new EmptyBorder(5, 14, 5, 14)
        ));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JLabel sectionHeader(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Consolas", Font.BOLD, 10));
        l.setForeground(TEXT_MUTED);
        l.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(0, 0, 4, 0)
        ));
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private JLabel consoleLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Consolas", Font.BOLD, 10));
        l.setForeground(TEXT_MUTED);
        return l;
    }

    // ================== MAIN ==================
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new DefenceCommandDashboard().setVisible(true));
    }
}
