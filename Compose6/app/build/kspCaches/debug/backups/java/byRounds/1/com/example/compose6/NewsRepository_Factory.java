package com.example.compose6;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class NewsRepository_Factory implements Factory<NewsRepository> {
  private final Provider<NewsApiService> apiServiceProvider;

  public NewsRepository_Factory(Provider<NewsApiService> apiServiceProvider) {
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public NewsRepository get() {
    return newInstance(apiServiceProvider.get());
  }

  public static NewsRepository_Factory create(Provider<NewsApiService> apiServiceProvider) {
    return new NewsRepository_Factory(apiServiceProvider);
  }

  public static NewsRepository newInstance(NewsApiService apiService) {
    return new NewsRepository(apiService);
  }
}
