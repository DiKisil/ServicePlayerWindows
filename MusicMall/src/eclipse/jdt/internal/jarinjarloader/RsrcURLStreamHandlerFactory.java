/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eclipse.jdt.internal.jarinjarloader;

/**
 *
 * @author Diana
 */
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class RsrcURLStreamHandlerFactory
  implements URLStreamHandlerFactory
{
  private ClassLoader classLoader;
  private URLStreamHandlerFactory chainFac;
  
  public RsrcURLStreamHandlerFactory(ClassLoader cl)
  {
    this.classLoader = cl;
  }
  
  public URLStreamHandler createURLStreamHandler(String protocol)
  {
    if ("rsrc".equals(protocol)) {
      return new RsrcURLStreamHandler(this.classLoader);
    }
    if (this.chainFac != null) {
      return this.chainFac.createURLStreamHandler(protocol);
    }
    return null;
  }
  
  public void setURLStreamHandlerFactory(URLStreamHandlerFactory fac)
  {
    this.chainFac = fac;
  }
}
