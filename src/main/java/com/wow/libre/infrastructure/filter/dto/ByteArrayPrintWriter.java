package com.wow.libre.infrastructure.filter.dto;

import jakarta.servlet.*;

import java.io.*;

public class ByteArrayPrintWriter {
  private ByteArrayOutputStream biteArray = new ByteArrayOutputStream();

  private PrintWriter pw = new PrintWriter(biteArray);

  private ServletOutputStream sos = new ByteArrayServletStream(biteArray);

  public PrintWriter getWriter() {
    return pw;
  }

  public ServletOutputStream getStream() {
    return sos;
  }

  public byte[] toByteArray() {
    return biteArray.toByteArray();
  }
}
