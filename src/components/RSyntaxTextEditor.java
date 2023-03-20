package components;

import net.miginfocom.swing.MigLayout;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class RSyntaxTextEditor {
    public final JPanel container;
    public final RSyntaxTextArea textArea;
    public final RTextScrollPane textAreaScrollPane;

    public static final Map<String, String> SYNTAX_STYLES_MAP = new HashMap<>() {{
        // Referenced from https://gist.github.com/ppisarczyk/43962d06686722d26d176fad46879d41

        put("txt", SyntaxConstants.SYNTAX_STYLE_NONE);

        put("as", SyntaxConstants.SYNTAX_STYLE_ACTIONSCRIPT);

        put("asm", SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_6502);
        put("a51", SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_6502);
        put("inc", SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_6502);
        put("nasm", SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_6502);

        put("c", SyntaxConstants.SYNTAX_STYLE_C);
        put("h", SyntaxConstants.SYNTAX_STYLE_C);

        put("clj", SyntaxConstants.SYNTAX_STYLE_CLOJURE);
        put("boot", SyntaxConstants.SYNTAX_STYLE_CLOJURE);
        put("cl2", SyntaxConstants.SYNTAX_STYLE_CLOJURE);
        put("cljc", SyntaxConstants.SYNTAX_STYLE_CLOJURE);
        put("cljs", SyntaxConstants.SYNTAX_STYLE_CLOJURE);
        put("cljscm", SyntaxConstants.SYNTAX_STYLE_CLOJURE);
        put("cljx", SyntaxConstants.SYNTAX_STYLE_CLOJURE);
        put("hic", SyntaxConstants.SYNTAX_STYLE_CLOJURE);

        put("cpp", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        put("hpp", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);

        put("cs", SyntaxConstants.SYNTAX_STYLE_CSHARP);
        put("cake", SyntaxConstants.SYNTAX_STYLE_CSHARP);
        put("cshtml", SyntaxConstants.SYNTAX_STYLE_CSHARP);
        put("csx", SyntaxConstants.SYNTAX_STYLE_CSHARP);

        put("css", SyntaxConstants.SYNTAX_STYLE_CSS);

        put("csv", SyntaxConstants.SYNTAX_STYLE_CSV);

        put("d", SyntaxConstants.SYNTAX_STYLE_D);
        put("di", SyntaxConstants.SYNTAX_STYLE_D);

        put("dockerfile", SyntaxConstants.SYNTAX_STYLE_DOCKERFILE);

        put("dart", SyntaxConstants.SYNTAX_STYLE_DART);

        put("f90", SyntaxConstants.SYNTAX_STYLE_FORTRAN);
        put("f", SyntaxConstants.SYNTAX_STYLE_FORTRAN);
        put("f03", SyntaxConstants.SYNTAX_STYLE_FORTRAN);
        put("f08", SyntaxConstants.SYNTAX_STYLE_FORTRAN);
        put("f77", SyntaxConstants.SYNTAX_STYLE_FORTRAN);
        put("f95", SyntaxConstants.SYNTAX_STYLE_FORTRAN);
        put("for", SyntaxConstants.SYNTAX_STYLE_FORTRAN);
        put("fpp", SyntaxConstants.SYNTAX_STYLE_FORTRAN);

        put("go", SyntaxConstants.SYNTAX_STYLE_GO);

        put("groovy", SyntaxConstants.SYNTAX_STYLE_GROOVY);
        put("grt", SyntaxConstants.SYNTAX_STYLE_GROOVY);
        put("gtpl", SyntaxConstants.SYNTAX_STYLE_GROOVY);
        put("gvy", SyntaxConstants.SYNTAX_STYLE_GROOVY);

        put("html", SyntaxConstants.SYNTAX_STYLE_HTML);
        put("htm", SyntaxConstants.SYNTAX_STYLE_HTML);
        put("st", SyntaxConstants.SYNTAX_STYLE_HTML);
        put("xht", SyntaxConstants.SYNTAX_STYLE_HTML);
        put("xhtml", SyntaxConstants.SYNTAX_STYLE_HTML);

        put("ini", SyntaxConstants.SYNTAX_STYLE_INI);
        put("cfg", SyntaxConstants.SYNTAX_STYLE_INI);
        put("prefs", SyntaxConstants.SYNTAX_STYLE_INI);
        put("pro", SyntaxConstants.SYNTAX_STYLE_INI);
        put("properties", SyntaxConstants.SYNTAX_STYLE_INI);

        put("java", SyntaxConstants.SYNTAX_STYLE_JAVA);

        put("js", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("_js", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("bones", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("es", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("es6", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("frag", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("gs", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("jake", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("jsb", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("jscad", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("jsfl", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("jsm", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("jss", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("njs", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("pac", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("sjs", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("ssjs", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("sublime-build", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("sublime-commands", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("sublime-completions", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("sublime-keymap", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("sublime-macro", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("sublime-menu", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("sublime-mousemap", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("sublime-project", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("sublime-settings", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("sublime-theme", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("sublime-workspace", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("sublime_metrics", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("sublime_session", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("xsjs", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        put("xsjslib", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);

        put("json", SyntaxConstants.SYNTAX_STYLE_JSON);
        put("geojson", SyntaxConstants.SYNTAX_STYLE_JSON);
        put("lock", SyntaxConstants.SYNTAX_STYLE_JSON);
        put("topojson", SyntaxConstants.SYNTAX_STYLE_JSON);

        put("jsp", SyntaxConstants.SYNTAX_STYLE_JSP);

        put("kt", SyntaxConstants.SYNTAX_STYLE_KOTLIN);
        put("ktm", SyntaxConstants.SYNTAX_STYLE_KOTLIN);
        put("kts", SyntaxConstants.SYNTAX_STYLE_KOTLIN);

        put("less", SyntaxConstants.SYNTAX_STYLE_LESS);

        put("lisp", SyntaxConstants.SYNTAX_STYLE_LISP);
        put("asd", SyntaxConstants.SYNTAX_STYLE_LISP);
        put("cl", SyntaxConstants.SYNTAX_STYLE_LISP);
        put("l", SyntaxConstants.SYNTAX_STYLE_LISP);
        put("lsp", SyntaxConstants.SYNTAX_STYLE_LISP);
        put("ny", SyntaxConstants.SYNTAX_STYLE_LISP);
        put("podsl", SyntaxConstants.SYNTAX_STYLE_LISP);
        put("sexp", SyntaxConstants.SYNTAX_STYLE_LISP);

        put("lua", SyntaxConstants.SYNTAX_STYLE_LUA);
        put("fcgi", SyntaxConstants.SYNTAX_STYLE_LUA);
        put("nse", SyntaxConstants.SYNTAX_STYLE_LUA);
        put("pd_lua", SyntaxConstants.SYNTAX_STYLE_LUA);
        put("rbxs", SyntaxConstants.SYNTAX_STYLE_LUA);
        put("wlua", SyntaxConstants.SYNTAX_STYLE_LUA);

        put("mak", SyntaxConstants.SYNTAX_STYLE_MAKEFILE);
        put("mk", SyntaxConstants.SYNTAX_STYLE_MAKEFILE);
        put("mkfile", SyntaxConstants.SYNTAX_STYLE_MAKEFILE);

        put("md", SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
        put("markdown", SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
        put("mkd", SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
        put("mkdn", SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
        put("mkdown", SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
        put("ron", SyntaxConstants.SYNTAX_STYLE_MARKDOWN);

        put("xml", SyntaxConstants.SYNTAX_STYLE_XML);
        put("ant", SyntaxConstants.SYNTAX_STYLE_XML);
        put("axml", SyntaxConstants.SYNTAX_STYLE_XML);
        put("ccxml", SyntaxConstants.SYNTAX_STYLE_XML);
        put("clixml", SyntaxConstants.SYNTAX_STYLE_XML);
        put("cproject", SyntaxConstants.SYNTAX_STYLE_XML);
        put("csl", SyntaxConstants.SYNTAX_STYLE_XML);
        put("csproj", SyntaxConstants.SYNTAX_STYLE_XML);
        put("ct", SyntaxConstants.SYNTAX_STYLE_XML);
        put("dita", SyntaxConstants.SYNTAX_STYLE_XML);
        put("ditamap", SyntaxConstants.SYNTAX_STYLE_XML);
        put("ditaval", SyntaxConstants.SYNTAX_STYLE_XML);
        put("dll.config", SyntaxConstants.SYNTAX_STYLE_XML);
        put("dotsettings", SyntaxConstants.SYNTAX_STYLE_XML);
        put("filters", SyntaxConstants.SYNTAX_STYLE_XML);
        put("fsproj", SyntaxConstants.SYNTAX_STYLE_XML);
        put("fxml", SyntaxConstants.SYNTAX_STYLE_XML);
        put("glade", SyntaxConstants.SYNTAX_STYLE_XML);
        put("gml", SyntaxConstants.SYNTAX_STYLE_XML);
        put("grxml", SyntaxConstants.SYNTAX_STYLE_XML);
        put("iml", SyntaxConstants.SYNTAX_STYLE_XML);
        put("ivy", SyntaxConstants.SYNTAX_STYLE_XML);
        put("jelly", SyntaxConstants.SYNTAX_STYLE_XML);
        put("jsproj", SyntaxConstants.SYNTAX_STYLE_XML);
        put("kml", SyntaxConstants.SYNTAX_STYLE_XML);
        put("launch", SyntaxConstants.SYNTAX_STYLE_XML);
        put("mdpolicy", SyntaxConstants.SYNTAX_STYLE_XML);
        put("mm", SyntaxConstants.SYNTAX_STYLE_XML);
        put("mod", SyntaxConstants.SYNTAX_STYLE_XML);
        put("mxml", SyntaxConstants.SYNTAX_STYLE_XML);
        put("nproj", SyntaxConstants.SYNTAX_STYLE_XML);
        put("nuspec", SyntaxConstants.SYNTAX_STYLE_XML);
        put("odd", SyntaxConstants.SYNTAX_STYLE_XML);
        put("osm", SyntaxConstants.SYNTAX_STYLE_XML);
        put("plist", SyntaxConstants.SYNTAX_STYLE_XML);
        put("pluginspec", SyntaxConstants.SYNTAX_STYLE_XML);
        put("props", SyntaxConstants.SYNTAX_STYLE_XML);
        put("ps1xml", SyntaxConstants.SYNTAX_STYLE_XML);
        put("psc1", SyntaxConstants.SYNTAX_STYLE_XML);
        put("pt", SyntaxConstants.SYNTAX_STYLE_XML);
        put("rdf", SyntaxConstants.SYNTAX_STYLE_XML);
        put("rss", SyntaxConstants.SYNTAX_STYLE_XML);
        put("scxml", SyntaxConstants.SYNTAX_STYLE_XML);
        put("srdf", SyntaxConstants.SYNTAX_STYLE_XML);
        put("storyboard", SyntaxConstants.SYNTAX_STYLE_XML);
        put("stTheme", SyntaxConstants.SYNTAX_STYLE_XML);
        put("sublime-snippet", SyntaxConstants.SYNTAX_STYLE_XML);
        put("targets", SyntaxConstants.SYNTAX_STYLE_XML);
        put("tmCommand", SyntaxConstants.SYNTAX_STYLE_XML);
        put("tml", SyntaxConstants.SYNTAX_STYLE_XML);
        put("tmLanguage", SyntaxConstants.SYNTAX_STYLE_XML);
        put("tmPreferences", SyntaxConstants.SYNTAX_STYLE_XML);
        put("tmSnippet", SyntaxConstants.SYNTAX_STYLE_XML);
        put("tmTheme", SyntaxConstants.SYNTAX_STYLE_XML);
        put("ui", SyntaxConstants.SYNTAX_STYLE_XML);
        put("urdf", SyntaxConstants.SYNTAX_STYLE_XML);
        put("ux", SyntaxConstants.SYNTAX_STYLE_XML);
        put("vbproj", SyntaxConstants.SYNTAX_STYLE_XML);
        put("vcxproj", SyntaxConstants.SYNTAX_STYLE_XML);
        put("vssettings", SyntaxConstants.SYNTAX_STYLE_XML);
        put("vxml", SyntaxConstants.SYNTAX_STYLE_XML);
        put("wsdl", SyntaxConstants.SYNTAX_STYLE_XML);
        put("wsf", SyntaxConstants.SYNTAX_STYLE_XML);
        put("wxi", SyntaxConstants.SYNTAX_STYLE_XML);
        put("wxl", SyntaxConstants.SYNTAX_STYLE_XML);
        put("wxs", SyntaxConstants.SYNTAX_STYLE_XML);
        put("x3d", SyntaxConstants.SYNTAX_STYLE_XML);
        put("xacro", SyntaxConstants.SYNTAX_STYLE_XML);
        put("xaml", SyntaxConstants.SYNTAX_STYLE_XML);
        put("xib", SyntaxConstants.SYNTAX_STYLE_XML);
        put("xlf", SyntaxConstants.SYNTAX_STYLE_XML);
        put("xliff", SyntaxConstants.SYNTAX_STYLE_XML);
        put("xmi", SyntaxConstants.SYNTAX_STYLE_XML);
        put("xml.dist", SyntaxConstants.SYNTAX_STYLE_XML);
        put("xproj", SyntaxConstants.SYNTAX_STYLE_XML);
        put("xsd", SyntaxConstants.SYNTAX_STYLE_XML);
        put("xul", SyntaxConstants.SYNTAX_STYLE_XML);
        put("zcml", SyntaxConstants.SYNTAX_STYLE_XML);

        put("nsi", SyntaxConstants.SYNTAX_STYLE_NSIS);
        put("nsh", SyntaxConstants.SYNTAX_STYLE_NSIS);

        put("pl", SyntaxConstants.SYNTAX_STYLE_PERL);
        put("al", SyntaxConstants.SYNTAX_STYLE_PERL);
        put("cgi", SyntaxConstants.SYNTAX_STYLE_PERL);
        put("perl", SyntaxConstants.SYNTAX_STYLE_PERL);
        put("ph", SyntaxConstants.SYNTAX_STYLE_PERL);
        put("plx", SyntaxConstants.SYNTAX_STYLE_PERL);
        put("pm", SyntaxConstants.SYNTAX_STYLE_PERL);
        put("pod", SyntaxConstants.SYNTAX_STYLE_PERL);
        put("psgi", SyntaxConstants.SYNTAX_STYLE_PERL);
        put("t", SyntaxConstants.SYNTAX_STYLE_PERL);

        put("php", SyntaxConstants.SYNTAX_STYLE_PHP);
        put("aw", SyntaxConstants.SYNTAX_STYLE_PHP);
        put("ctp", SyntaxConstants.SYNTAX_STYLE_PHP);
        put("php3", SyntaxConstants.SYNTAX_STYLE_PHP);
        put("php4", SyntaxConstants.SYNTAX_STYLE_PHP);
        put("php5", SyntaxConstants.SYNTAX_STYLE_PHP);
        put("phps", SyntaxConstants.SYNTAX_STYLE_PHP);
        put("phpt", SyntaxConstants.SYNTAX_STYLE_PHP);

        put("proto", SyntaxConstants.SYNTAX_STYLE_PROTO);

        put("py", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        put("bzl", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        put("gyp", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        put("lmi", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        put("pyde", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        put("pyp", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        put("pyt", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        put("pyw", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        put("rpy", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        put("tac", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        put("wsgi", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        put("xpy", SyntaxConstants.SYNTAX_STYLE_PYTHON);

        put("rb", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("builder", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("gemspec", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("god", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("irbrc", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("jbuilder", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("mspec", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("podspec", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("rabl", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("rake", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("rbuild", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("rbw", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("rbx", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("ru", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("ruby", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("thor", SyntaxConstants.SYNTAX_STYLE_RUBY);
        put("watchr", SyntaxConstants.SYNTAX_STYLE_RUBY);

        put("sas", SyntaxConstants.SYNTAX_STYLE_SAS);

        put("scala", SyntaxConstants.SYNTAX_STYLE_SCALA);
        put("sbt", SyntaxConstants.SYNTAX_STYLE_SCALA);
        put("sc", SyntaxConstants.SYNTAX_STYLE_SCALA);

        put("sql", SyntaxConstants.SYNTAX_STYLE_SQL);
        put("cql", SyntaxConstants.SYNTAX_STYLE_SQL);
        put("ddl", SyntaxConstants.SYNTAX_STYLE_SQL);
        put("prc", SyntaxConstants.SYNTAX_STYLE_SQL);
        put("tab", SyntaxConstants.SYNTAX_STYLE_SQL);
        put("udf", SyntaxConstants.SYNTAX_STYLE_SQL);
        put("viw", SyntaxConstants.SYNTAX_STYLE_SQL);

        put("tcl", SyntaxConstants.SYNTAX_STYLE_TCL);
        put("adp", SyntaxConstants.SYNTAX_STYLE_TCL);
        put("tm", SyntaxConstants.SYNTAX_STYLE_TCL);

        put("ts", SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT);
        put("tsx", SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT);

        put("sh", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
        put("bash", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
        put("bats", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
        put("command", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
        put("ksh", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
        put("sh.in", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
        put("tmux", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
        put("tool", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
        put("zsh", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);

        put("vb", SyntaxConstants.SYNTAX_STYLE_VISUAL_BASIC);
        put("bas", SyntaxConstants.SYNTAX_STYLE_VISUAL_BASIC);
        put("cls", SyntaxConstants.SYNTAX_STYLE_VISUAL_BASIC);
        put("frm", SyntaxConstants.SYNTAX_STYLE_VISUAL_BASIC);
        put("frx", SyntaxConstants.SYNTAX_STYLE_VISUAL_BASIC);
        put("vba", SyntaxConstants.SYNTAX_STYLE_VISUAL_BASIC);
        put("vbhtml", SyntaxConstants.SYNTAX_STYLE_VISUAL_BASIC);
        put("vbs", SyntaxConstants.SYNTAX_STYLE_VISUAL_BASIC);

        put("bat", SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH);
        put("cmd", SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH);

        put("yml", SyntaxConstants.SYNTAX_STYLE_YAML);
        put("reek", SyntaxConstants.SYNTAX_STYLE_YAML);
        put("rviz", SyntaxConstants.SYNTAX_STYLE_YAML);
        put("sublime-syntax", SyntaxConstants.SYNTAX_STYLE_YAML);
        put("syntax", SyntaxConstants.SYNTAX_STYLE_YAML);
        put("yaml", SyntaxConstants.SYNTAX_STYLE_YAML);
        put("yaml-tmlanguage", SyntaxConstants.SYNTAX_STYLE_YAML);
    }};
    public static final String SYNTAX_STYLES_FALLBACK = SyntaxConstants.SYNTAX_STYLE_NONE;

    public RSyntaxTextEditor() {
        textArea = new RSyntaxTextArea(20, 60) {{
            setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
            setCodeFoldingEnabled(true);
        }};
        textAreaScrollPane = new RTextScrollPane(textArea);
        container = new JPanel(new MigLayout("insets 0 0 0 0")) {{
            add(textAreaScrollPane, "width 100%, height 100%");
        }};
    }

    public void setSyntaxEditingStyle(final String extension) {
        textArea.setSyntaxEditingStyle(SYNTAX_STYLES_MAP.getOrDefault(extension.trim().toLowerCase(), SYNTAX_STYLES_FALLBACK));
    }
}
