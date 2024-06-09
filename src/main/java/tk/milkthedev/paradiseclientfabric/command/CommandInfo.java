package tk.milkthedev.paradiseclientfabric.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE_USE;

@Target(value = TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

    String alias();

    String description() default "";

    String usage() default "";

    String[] aliases() default {};
}