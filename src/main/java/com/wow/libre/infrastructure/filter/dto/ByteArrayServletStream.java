package com.wow.libre.infrastructure.filter.dto;

import jakarta.servlet.*;

import java.io.*;

public class ByteArrayServletStream extends ServletOutputStream {
  ByteArrayOutputStream baos;

  public ByteArrayServletStream(ByteArrayOutputStream baos) {
    this.baos = baos;
  }

  @Override
  public void write(int param) throws IOException {
    baos.write(param);
  }

  @Override
  public boolean isReady() {
    return false;
  }

  @Override
  public void setWriteListener(WriteListener listener) {
  }
}
