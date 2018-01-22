package ru.sfedu.projectmanager.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration utility. Allows to get configuration properties from the
 * default configuration file
 *
 * @author Boris Jmailov
 */
public class ConfigurationUtil {
    private static Logger logger = Logger.getLogger(ConfigurationUtil.class);
    private static  String configPath = "E:\\ProjectManager\\src\\main\\resources\\config.properties";
    private static final Properties configuration = new Properties();

    /**
     * Hides default constructor
     */
    private ConfigurationUtil() {
    }
    
    private static Properties getConfiguration() throws IOException {
        if (configuration.isEmpty()){
            loadConfiguration();
        }
        return configuration;
    }

    /**
     * Loads configuration from <code>DEFAULT_CONFIG_PATH</code>
     * @throws IOException In case of the configuration file read failure
     */
    private static void loadConfiguration() throws IOException{
        try (FileInputStream in = new FileInputStream(configPath)) {
            configuration.load(in);
        } catch (IOException ex) {
            throw new IOException(ex);
        }
    }
    /**
     * Gets configuration entry value
     * @param key Entry key
     * @return Entry value by key
     * @throws IOException In case of the configuration file read failure
     */
    public static String getConfigurationEntry(String key) throws IOException{
//        logger.info(getConfiguration().getProperty(key));
        return getConfiguration().getProperty(key);
    }

    public static void setConfigPath (String config_path){
        if (config_path != null){
                File file = new File(config_path);
                if (file.exists() && file.isFile()){
                    configPath = config_path;
                } else {
                    System.out.println("Config file not exist. Default settings have been set");
                }
        }
    }

//    public static String getConfigPath (){
//        return configPath
//    }

}
