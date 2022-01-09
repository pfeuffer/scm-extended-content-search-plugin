package de.pfeufferweb.scm.ecs;

import com.google.common.io.Resources;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sonia.scm.plugin.PluginLoader;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TikaFileContentExtractorTest {

  @Mock
  private PluginLoader pluginLoader;

  @Test
  void shouldConvertPdf() throws IOException {
    TikaFileContentExtractor contentExtractor = new TikaFileContentExtractor(pluginLoader);
    InputStream pdfInput = Resources.getResource("test.pdf").openStream();

    String text = contentExtractor.extractText(pdfInput);

    assertThat(text).contains("simple text in pdf");
  }
}
