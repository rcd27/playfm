package com.rcd27.playfm;

import androidx.annotation.NonNull;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import java.lang.reflect.Method;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Подсмотрено и взято у https://github.com/artem-zinnatullin/qualitymatters
 */

/*
 FIXME(#1): сходу написать подобное на Котлине не получилось из-за проблем в понимании интеропа
 с джавой: при обращении к полям defaultConfig, помпилятор кастует классы к `KClass`. Попытки
 передать их в конструктор `Config.Implementation` приводят к ошибки компиляции, так как тот ждёт
 от нас джавовые Class.
 */
public class AppRobolectricTestRunner extends RobolectricTestRunner {

  private static final int SDK_EMULATE_LEVEL = 23;

  public AppRobolectricTestRunner(@NonNull Class<?> klass) throws Exception {
    super(klass);
  }

  @BeforeClass
  static void setUp() {
    RxJavaPlugins.onIoScheduler(Schedulers.computation());
  }

  @AfterClass
  static void tearDown() {
    RxJavaPlugins.reset();
  }

  @Override
  public Config getConfig(@NonNull Method method) {
    final Config defaultConfig = super.getConfig(method);
    return new Config.Implementation(
        new int[]{SDK_EMULATE_LEVEL},
        defaultConfig.manifest(),
        defaultConfig.qualifiers(),
        defaultConfig.packageName(),
        defaultConfig.abiSplit(),
        defaultConfig.resourceDir(),
        defaultConfig.assetDir(),
        defaultConfig.buildDir(),
        defaultConfig.shadows(),
        defaultConfig.instrumentedPackages(),
        UnitTestApp.class,
        defaultConfig.libraries(),
        defaultConfig.constants() == Void.class ? BuildConfig.class : defaultConfig.constants()
    );
  }

  @NonNull
  public static void pikabuTestcaseApp() {
  }
}
