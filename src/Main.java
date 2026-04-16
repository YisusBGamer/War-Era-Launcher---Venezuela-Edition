import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class Main {
    
    private static String APP_URL = "https://app.warera.io";
    private static final int CACHE_CLEAR_DAYS = 7;
    private static final String CACHE_DIR = "cache_data";
    private static final String LAST_CACHE_CLEAR_FILE = "last_cache_clear.txt";
    private static final long REFRESH_INTERVAL_MS = 30 * 60 * 1000; // 30 minutes

    private static final String CHROME_PATH_WIN = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
    private static final String EDGE_PATH_WIN = "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe";
    private static final String FIREFOX_PATH_WIN = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    private static final String OPERA_PATH_WIN = "C:\\Program Files\\Opera\\Opera\\opera.exe";
    private static final String SAFARI_PATH_WIN = "C:\\Program Files\\Safari\\Safari.exe";
    private static final String BRAVE_PATH_WIN = "C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe";
    
    // Linux browser paths
    private static final String CHROME_PATH_LINUX = "/usr/bin/google-chrome";
    private static final String EDGE_PATH_LINUX = "/usr/bin/microsoft-edge";
    private static final String FIREFOX_PATH_LINUX = "/usr/bin/firefox";
    private static final String OPERA_PATH_LINUX = "/usr/bin/opera";
    private static final String BRAVE_PATH_LINUX = "/usr/bin/brave-browser";
    
    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("windows");
    private boolean useLinuxMode = !IS_WINDOWS; // Auto-detectar sistema operativo
    
    private String selectedBrowser = "chrome";
    private String currentLanguage = "es";
    private boolean autoOpen = false;
    
    // Multi-language support
    private java.util.Map<String, java.util.Map<String, String>> translations;
    
    private Timer cacheCheckTimer;
    private Timer refreshTimer;
    private JFrame frame;
    private JTextField urlField;
    private JPanel browserPanel;
    private JButton btnChrome, btnEdge, btnFirefox, btnOpera, btnSafari, btnBrave;
    private JButton btnEs, btnEn, btnPt, btnFr;
    private JLabel titleLabel, subLabel, browserLabel, footerLabel;
    private JLabel usersOnlineLabel;
    private JPanel quickLinks;
    private JButton btnPlay, btnYisus, btnEconomia, btnSimulador, btnDiscord;
    
    // Dark theme colors
    private Color bgDark = new Color(20, 20, 20);
    private Color bgLight = new Color(40, 40, 40);
    private Color accent = new Color(0, 120, 215);
    private Color fgLight = new Color(220, 220, 220);
    private Color fgMuted = new Color(120, 120, 120);
    
    public static void main(String[] args) {
        // Check Java version
        String javaVersion = System.getProperty("java.version");
        int majorVersion = getMajorJavaVersion(javaVersion);
        
        if (majorVersion < 8) {
            JOptionPane.showMessageDialog(null,
                "================================================\n" +
                " ERROR: Versión de Java incompatible\n" +
                "================================================\n\n" +
                "Tu versión de Java: " + javaVersion + "\n" +
                "Se requiere: JDK 8, 11 o 17 (o superior)\n\n" +
                "Descarga Java desde:\n" +
                "https://www.oracle.com/java/technologies/downloads/\n\n" +
                "================================================",
                "War Era Launcher - Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        Main app = new Main();
        app.initTranslations();
        app.init();
    }
    
    private static int getMajorJavaVersion(String version) {
        // Java version format: 1.8.0_xxx, 11.0.x, 17.0.x, etc.
        if (version.startsWith("1.")) {
            return Integer.parseInt(version.substring(2, 3));
        }
        String[] parts = version.split("\\.");
        return Integer.parseInt(parts[0]);
    }
    
    private void initTranslations() {
        translations = new java.util.HashMap<>();
        
        // Spanish
        java.util.Map<String, String> es = new java.util.HashMap<>();
        es.put("title", "BIENVENIDO A WAR ERA LAUNCHER");
        es.put("subtitle", "Creado por YisusBOficial para la comunidad WarEra Venezuela");
        es.put("browserLabel", "Seleccionar navegador:");
        es.put("osLabel", "Sistema:");
        es.put("osWindows", "Windows");
        es.put("osLinux", "Linux/Ubuntu");
        es.put("footer", "Cache se limpia automáticamente cada 7 días");
        es.put("usersOnline", "🎮 War Era Launcher");
        es.put("langLabel", "Idioma:");
        es.put("btnPlay", "Jugar War Era");
        es.put("btnYisus", "YisusBOficial");
        es.put("btnEconomia", "Economia");
        es.put("btnSimulador", "Warera Simulador");
        es.put("btnDiscord", "Unirse al Discord");
        es.put("creative", "Lanzador gratuito - No está a la venta - Hecho por YisusOficial");
        translations.put("es", es);
        
        // English
        java.util.Map<String, String> en = new java.util.HashMap<>();
        en.put("title", "WELCOME TO WAR ERA LAUNCHER");
        en.put("subtitle", "Created by YisusBOficial for the WarEra Venezuela community");
        en.put("browserLabel", "Select browser:");
        en.put("osLabel", "System:");
        en.put("osWindows", "Windows");
        en.put("osLinux", "Linux/Ubuntu");
        en.put("footer", "Cache is automatically cleared every 7 days");
        en.put("usersOnline", "🎮 War Era Launcher");
        en.put("langLabel", "Language:");
        en.put("btnPlay", "Play War Era");
        en.put("btnYisus", "YisusBOficial");
        en.put("btnEconomia", "Economy");
        en.put("btnSimulador", "Warera Simulator");
        en.put("btnDiscord", "Join Discord");
        en.put("creative", "Free launcher - Not for sale - Made by YisusOficial");
        translations.put("en", en);
        
        // Portuguese
        java.util.Map<String, String> pt = new java.util.HashMap<>();
        pt.put("title", "BEM-VINDO AO WAR ERA LAUNCHER");
        pt.put("subtitle", "Criado por YisusBOficial para a comunidade WarEra Venezuela");
        pt.put("browserLabel", "Selecionar navegador:");
        pt.put("osLabel", "Sistema:");
        pt.put("osWindows", "Windows");
        pt.put("osLinux", "Linux/Ubuntu");
        pt.put("footer", "Cache é limpo automaticamente a cada 7 dias");
        pt.put("usersOnline", "🎮 War Era Launcher");
        pt.put("langLabel", "Idioma:");
        pt.put("btnPlay", "Jogar War Era");
        pt.put("btnYisus", "YisusBOficial");
        pt.put("btnEconomia", "Economia");
        pt.put("btnSimulador", "Warera Simulador");
        pt.put("btnDiscord", "Entrar no Discord");
        pt.put("creative", "Launcher gratuito - Não está à venda - Feito por YisusOficial");
        translations.put("pt", pt);
        
        // French
        java.util.Map<String, String> fr = new java.util.HashMap<>();
        fr.put("title", "BIENVENUE SUR WAR ERA LAUNCHER");
        fr.put("subtitle", "Créé par YisusBOficial pour la communauté WarEra Venezuela");
        fr.put("browserLabel", "Sélectionner le navigateur:");
        fr.put("osLabel", "Système:");
        fr.put("osWindows", "Windows");
        fr.put("osLinux", "Linux/Ubuntu");
        fr.put("footer", "Le cache est automatiquement effacé tous les 7 jours");
        fr.put("usersOnline", "🎮 War Era Launcher");
        fr.put("langLabel", "Langue:");
        fr.put("btnPlay", "Jouer War Era");
        fr.put("btnYisus", "YisusBOficial");
        fr.put("btnEconomia", "Économie");
        fr.put("btnSimulador", "Warera Simulateur");
        fr.put("btnDiscord", "Rejoindre Discord");
        fr.put("creative", "Lanceur gratuit - Pas à vendre - Fait par YisusOficial");
        translations.put("fr", fr);
    }
    
    private String t(String key) {
        java.util.Map<String, String> lang = translations.get(currentLanguage);
        return lang != null ? lang.get(key) : translations.get("es").get(key);
    }
    
    private void init() {
        // Main frame
        frame = new JFrame("War Era Launcher");
        frame.setSize(1000, 550);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(bgDark);
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(bgLight);
        menuBar.setBorderPainted(false);
        
        // Language menu
        JMenu langMenu = new JMenu("🌐 Idioma");
        langMenu.setForeground(fgLight);
        langMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JMenuItem itemEs = new JMenuItem("Español");
        itemEs.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemEs.addActionListener(e -> changeLanguage("es"));
        JMenuItem itemEn = new JMenuItem("English");
        itemEn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemEn.addActionListener(e -> changeLanguage("en"));
        JMenuItem itemPt = new JMenuItem("Português");
        itemPt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemPt.addActionListener(e -> changeLanguage("pt"));
        JMenuItem itemFr = new JMenuItem("Français");
        itemFr.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        itemFr.addActionListener(e -> changeLanguage("fr"));
        
        langMenu.add(itemEs);
        langMenu.add(itemEn);
        langMenu.add(itemPt);
        langMenu.add(itemFr);
        
        menuBar.add(langMenu);
        frame.setJMenuBar(menuBar);
        
        // Main panel with grid layout - Left (info) and Right (buttons)
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 30, 30));
        mainPanel.setBackground(bgDark);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // ===== LEFT PANEL - Title, Browser Selection, Info =====
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(bgDark);
        leftPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title
        titleLabel = new JLabel(t("title"));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(accent);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtitle
        subLabel = new JLabel(t("subtitle"));
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(fgMuted);
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        Component spacer1 = Box.createVerticalStrut(25);
        
        // Browser selection label
        browserLabel = new JLabel(t("browserLabel"));
        browserLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        browserLabel.setForeground(fgLight);
        browserLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // OS Selector buttons
        JLabel osLabel = new JLabel(t("osLabel"));
        osLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        osLabel.setForeground(fgMuted);
        osLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel osPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        osPanel.setBackground(bgDark);
        osPanel.setMaximumSize(new Dimension(200, 35));
        
        JButton btnWindows = new JButton(t("osWindows"));
        JButton btnLinux = new JButton(t("osLinux"));
        
        btnWindows.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnWindows.setForeground(fgLight);
        btnWindows.setBackground(useLinuxMode ? bgLight : accent);
        btnWindows.setOpaque(true);
        btnWindows.setBorderPainted(false);
        btnWindows.setFocusPainted(false);
        btnWindows.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnWindows.addActionListener(e -> {
            useLinuxMode = false;
            btnWindows.setBackground(accent);
            btnLinux.setBackground(bgLight);
        });
        
        btnLinux.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnLinux.setForeground(fgLight);
        btnLinux.setBackground(useLinuxMode ? accent : bgLight);
        btnLinux.setOpaque(true);
        btnLinux.setBorderPainted(false);
        btnLinux.setFocusPainted(false);
        btnLinux.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLinux.addActionListener(e -> {
            useLinuxMode = true;
            btnLinux.setBackground(accent);
            btnWindows.setBackground(bgLight);
        });
        
        osPanel.add(btnWindows);
        osPanel.add(btnLinux);
        
        Component osSpacer = Box.createVerticalStrut(15);
        
        // Browser buttons panel
        JPanel browserPanelNew = new JPanel(new GridLayout(2, 3, 8, 8));
        browserPanelNew.setBackground(bgDark);
        browserPanelNew.setMaximumSize(new Dimension(350, 90));
        
        btnChrome = createBrowserButton("Chrome", "chrome");
        btnEdge = createBrowserButton("Edge", "edge");
        btnFirefox = createBrowserButton("Firefox", "firefox");
        btnOpera = createBrowserButton("Opera", "opera");
        btnSafari = createBrowserButton("Safari", "safari");
        btnBrave = createBrowserButton("Brave", "brave");
        
        browserPanelNew.add(btnChrome);
        browserPanelNew.add(btnEdge);
        browserPanelNew.add(btnFirefox);
        browserPanelNew.add(btnOpera);
        browserPanelNew.add(btnSafari);
        browserPanelNew.add(btnBrave);
        
        Component spacer2 = Box.createVerticalStrut(30);
        
        // Creative message
        JLabel creativeLabel = new JLabel(t("creative"));
        creativeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        creativeLabel.setForeground(new Color(70, 70, 70));
        creativeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        leftPanel.add(titleLabel);
        leftPanel.add(subLabel);
        leftPanel.add(spacer1);
        leftPanel.add(osLabel);
        leftPanel.add(osPanel);
        leftPanel.add(osSpacer);
        leftPanel.add(browserLabel);
        leftPanel.add(browserPanelNew);
        leftPanel.add(spacer2);
        leftPanel.add(creativeLabel);
        
        // Flag Counter - moved to left panel for better visibility
        JLabel flagCounterLabel = new JLabel("<html><center><a href='https://info.flagcounter.com/iR0Q'><img src='https://s01.flagcounter.com/count2/iR0Q/bg_141414/txt_FFFFFF/border_333333/columns_4/maxflags_12/viewers_3/labels_0/pageviews_1/flags_0/percent_0/' alt='Flag Counter' border='0'></a></center></html>");
        flagCounterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        flagCounterLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        flagCounterLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        flagCounterLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://info.flagcounter.com/iR0Q"));
                } catch (Exception ex) {
                    System.out.println("Error opening URL: " + ex.getMessage());
                }
            }
        });
        leftPanel.add(flagCounterLabel);
        
        // ===== RIGHT PANEL - Action Buttons =====
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(bgDark);
        rightPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Section label
        JLabel sectionLabel = new JLabel("🎮 " + t("btnPlay"));
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionLabel.setForeground(fgLight);
        sectionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        Component spacer3 = Box.createVerticalStrut(25);
        
        // Quick buttons
        btnPlay = createQuickButton(t("btnPlay"), "https://app.warera.io");
        btnSimulador = createQuickButton(t("btnSimulador"), "https://war-era.vercel.app/");
        btnEconomia = createQuickButton(t("btnEconomia"), "https://war-era-economic-calculator-cbffbdb7.base44.app/");
        btnYisus = createQuickButton(t("btnYisus"), "https://www.facebook.com/YisusOficialGamer/");
        btnDiscord = createQuickButton(t("btnDiscord"), "https://discord.gg/DafG8cr6dZ");
        
        Dimension btnSize = new Dimension(280, 45);
        btnPlay.setMaximumSize(btnSize);
        btnSimulador.setMaximumSize(btnSize);
        btnEconomia.setMaximumSize(btnSize);
        btnYisus.setMaximumSize(btnSize);
        btnDiscord.setMaximumSize(btnSize);
        
        Component vs1 = Box.createVerticalStrut(12);
        Component vs2 = Box.createVerticalStrut(12);
        Component vs3 = Box.createVerticalStrut(12);
        Component vs4 = Box.createVerticalStrut(12);
        
        rightPanel.add(sectionLabel);
        rightPanel.add(spacer3);
        rightPanel.add(btnPlay);
        rightPanel.add(vs1);
        rightPanel.add(btnSimulador);
        rightPanel.add(vs2);
        rightPanel.add(btnEconomia);
        rightPanel.add(vs3);
        rightPanel.add(btnYisus);
        rightPanel.add(vs4);
        rightPanel.add(btnDiscord);
        
        // Add panels to main
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        
        frame.add(mainPanel);
        
        frame.setVisible(true);
        
        // Initialize cache
        initCache();
    }
    
    private void changeLanguage(String lang) {
        currentLanguage = lang;
        updateLabels();
        frame.setTitle("War Era Launcher");
        frame.repaint();
    }
    
    private JButton createQuickButton(String buttonName, String url) {
        JButton btn = new JButton(buttonName);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(fgLight);
        btn.setBackground(bgLight);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            openURL(url);
        });
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(accent);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgLight);
            }
        });
        
        return btn;
    }
    
    private JButton createBrowserButton(String browserName, String browserKey) {
        JButton btn = new JButton(browserName);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn.setForeground(fgLight);
        btn.setBackground(bgLight);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (selectedBrowser.equals(browserKey)) {
            btn.setBackground(accent);
        }
        
        btn.addActionListener(e -> {
            selectedBrowser = browserKey;
            updateBrowserButtons(btnChrome, btnEdge, btnFirefox, btnOpera, btnSafari, btnBrave);
            System.out.println("Navegador seleccionado: " + browserName);
        });
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(accent);
            }
            public void mouseExited(MouseEvent e) {
                if (!selectedBrowser.equals(browserKey)) {
                    btn.setBackground(bgLight);
                }
            }
        });
        
        return btn;
    }
    
    private void updateBrowserButtons(JButton chrome, JButton edge, JButton firefox, JButton opera, JButton safari, JButton brave) {
        chrome.setBackground(selectedBrowser.equals("chrome") ? accent : bgLight);
        edge.setBackground(selectedBrowser.equals("edge") ? accent : bgLight);
        firefox.setBackground(selectedBrowser.equals("firefox") ? accent : bgLight);
        opera.setBackground(selectedBrowser.equals("opera") ? accent : bgLight);
        safari.setBackground(selectedBrowser.equals("safari") ? accent : bgLight);
        brave.setBackground(selectedBrowser.equals("brave") ? accent : bgLight);
    }
    
    private JButton createLangButton(String langName, String langCode) {
        JButton btn = new JButton(langName);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn.setForeground(fgLight);
        btn.setBackground(bgLight);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (currentLanguage.equals(langCode)) {
            btn.setBackground(accent);
        }
        
        btn.addActionListener(e -> {
            currentLanguage = langCode;
            updateLabels();
            updateLangButtons(btn);
            System.out.println("Idioma seleccionado: " + langName);
        });
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(accent);
            }
            public void mouseExited(MouseEvent e) {
                if (!currentLanguage.equals(langCode)) {
                    btn.setBackground(bgLight);
                }
            }
        });
        
        return btn;
    }
    
    private void updateLabels() {
        titleLabel.setText(t("title"));
        subLabel.setText(t("subtitle"));
        browserLabel.setText(t("browserLabel"));
        btnPlay.setText(t("btnPlay"));
        btnYisus.setText(t("btnYisus"));
        btnEconomia.setText(t("btnEconomia"));
        btnSimulador.setText(t("btnSimulador"));
        
        // Update users online label
        if (usersOnlineLabel != null) {
            usersOnlineLabel.setText(t("usersOnline"));
        }
    }
    
    private void updateLangButtons(JButton selectedBtn) {
        btnEs.setBackground(currentLanguage.equals("es") ? accent : bgLight);
        btnEn.setBackground(currentLanguage.equals("en") ? accent : bgLight);
        btnPt.setBackground(currentLanguage.equals("pt") ? accent : bgLight);
        btnFr.setBackground(currentLanguage.equals("fr") ? accent : bgLight);
    }
    
    private void openURL() {
        // Default URL when no specific URL is provided
        openURL(APP_URL);
    }
    
    private void openURL(String url) {
        if (url != null && !url.trim().isEmpty()) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }
            
            boolean opened = false;
            
            try {
                String browserPath = getBrowserPath(selectedBrowser);
                if (browserPath != null && new File(browserPath).exists()) {
                    if (IS_WINDOWS) {
                        String[] cmd = {"cmd", "/c", "start", "", browserPath, "--app=" + url};
                        Runtime.getRuntime().exec(cmd);
                    } else {
                        // Linux
                        String[] cmd = {browserPath, url};
                        Runtime.getRuntime().exec(cmd);
                    }
                    opened = true;
                    System.out.println("Abriendo en " + selectedBrowser + ": " + url);
                }
            } catch (Exception e) {
                System.out.println("Error con " + selectedBrowser + ": " + e.getMessage());
            }
            
            if (!opened) {
                // Fallback to default browser
                try {
                    Desktop.getDesktop().browse(new URI(url));
                    System.out.println("Abriendo en navegador predeterminado: " + url);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        }
    }
    
    private String getBrowserPath(String browser) {
        if (!useLinuxMode) {
            switch (browser) {
                case "chrome": return CHROME_PATH_WIN;
                case "edge": return EDGE_PATH_WIN;
                case "firefox": return FIREFOX_PATH_WIN;
                case "opera": return OPERA_PATH_WIN;
                case "safari": return SAFARI_PATH_WIN;
                case "brave": return BRAVE_PATH_WIN;
                default: return CHROME_PATH_WIN;
            }
        } else {
            // Linux
            switch (browser) {
                case "chrome": return CHROME_PATH_LINUX;
                case "edge": return EDGE_PATH_LINUX;
                case "firefox": return FIREFOX_PATH_LINUX;
                case "opera": return OPERA_PATH_LINUX;
                case "safari": return null; // Safari no está disponible en Linux
                case "brave": return BRAVE_PATH_LINUX;
                default: return FIREFOX_PATH_LINUX;
            }
        }
    }
    
    private void initCache() {
        try {
            Files.createDirectories(Paths.get(CACHE_DIR));
            checkAndClearCache();
            
            cacheCheckTimer = new Timer(true);
            cacheCheckTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    checkAndClearCache();
                }
            }, 60 * 60 * 1000L, 60 * 60 * 1000L);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void checkAndClearCache() {
        try {
            Path lastClearPath = Paths.get(CACHE_DIR, LAST_CACHE_CLEAR_FILE);
            
            if (Files.exists(lastClearPath)) {
                String lastClearStr = new String(Files.readAllBytes(lastClearPath));
                long lastClearTime = Long.parseLong(lastClearStr.trim());
                long currentTime = System.currentTimeMillis();
                long daysDiff = (currentTime - lastClearTime) / (1000 * 60 * 60 * 24);
                
                if (daysDiff >= CACHE_CLEAR_DAYS) {
                    clearCache();
                    Files.write(lastClearPath, String.valueOf(currentTime).getBytes());
                }
            } else {
                Files.write(lastClearPath, String.valueOf(System.currentTimeMillis()).getBytes());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void clearCache() {
        try {
            String[] cachePaths = {
                System.getenv("LOCALAPPDATA") + "\\Google\\Chrome\\User Data\\Default\\Cache",
                System.getenv("LOCALAPPDATA") + "\\Google\\Chrome\\User Data\\Default\\Code Cache"
            };
            
            for (String path : cachePaths) {
                File dir = new File(path);
                if (dir.exists()) {
                    deleteDir(dir);
                }
            }
            System.out.println("Cache limpiada");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void deleteDir(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) deleteDir(f);
                    else f.delete();
                }
            }
            dir.delete();
        }
    }
}
