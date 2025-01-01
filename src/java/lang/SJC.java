package java.lang;

@SJC.IgnoreUnit
public @interface SJC {
  public int enterCodeAddr() default 0;
  public int offset() default 0;
  public int count() default 0;
  public static @interface Interrupt {}
  public static @interface Inline {}
  public static @interface NoInline {}
  public static @interface Head {}
  public static @interface Debug {}
  public static @interface Profile {}
  public static @interface NoProfile {}
  public static @interface StackExtreme {}
  public static @interface NoStackExtreme {}
  public static @interface PrintCode {}
  public static @interface NoOptimization {}
  public static @interface GenCode {}
  public static @interface SourceLines {}
  public static @interface ExplicitConversion {}
  public static @interface WinDLL {}
  public static @interface IgnoreUnit {}
  public static @interface InlineArrayVar {}
  public static @interface InlineArrayCount {}
}
