/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Config;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public abstract class VistaSingleton<T extends JFrame> {

    private static final Map<Class<?>, JFrame> instances = new HashMap<>();

    // Obtener instancia única
    public static <T extends JFrame> T getInstance(Class<T> clazz) {
        if (!instances.containsKey(clazz)) {
            try {
                Constructor<T> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                instances.put(clazz, constructor.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (T) instances.get(clazz);
    }

    // Reiniciar instancia
    public static void resetInstance(Class<?> clazz) {
        instances.remove(clazz);
    }
}
