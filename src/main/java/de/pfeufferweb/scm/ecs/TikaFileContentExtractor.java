/*
 * MIT License
 *
 * Copyright (c) 2020-present Cloudogu GmbH and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.pfeufferweb.scm.ecs;

import com.cloudogu.scm.search.FileContentExtractor;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.TikaCoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sonia.scm.io.ContentType;
import sonia.scm.plugin.Extension;
import sonia.scm.plugin.PluginLoader;

import javax.inject.Inject;
import java.io.InputStream;

@Extension
public class TikaFileContentExtractor implements FileContentExtractor {

  private static final Logger LOG = LoggerFactory.getLogger(TikaFileContentExtractor.class);

  private final ClassLoader uberClassLoader;

  @Inject
  public TikaFileContentExtractor(PluginLoader pluginLoader) {
    uberClassLoader = pluginLoader.getUberClassLoader();
  }

  @Override
  public boolean canHandle(ContentType contentType) {
    return "pdf".equals(contentType.getSecondary());
  }

  @Override
  public String extractText(InputStream content) {
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
//    Thread.currentThread().setContextClassLoader(uberClassLoader);
    try {
      try {
        System.out.println(TikaCoreProperties.SUBJECT);
        String s = new Tika(new TikaConfig(uberClassLoader)).parseToString(content);
        return s;
      } catch (Exception e) {
        LOG.error("Failed parsing file content", e);
        return "";
      }
    } finally {
      Thread.currentThread().setContextClassLoader(contextClassLoader);
    }
  }
}
