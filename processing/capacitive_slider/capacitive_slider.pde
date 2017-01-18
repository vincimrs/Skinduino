import ddf.minim.spi.*;
import ddf.minim.signals.*;
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.ugens.*;
import ddf.minim.effects.*;

import processing.serial.*;
import javax.swing.*;
import java.io.*;

static final int NUM_BUTTONS = 6;
static final int BUTTON_WIDTH = 75, BUTTON_HEIGHT = 75;
CapButton[] buttons;
Serial arduinoPort;
float sliderPos;
boolean show;

void setup() {
  size(displayWidth, displayHeight);
  if (frame != null) {
    frame.setResizable(true);
  }
  background(255);
   
  // load serial list
  String[] ports = Serial.list();
  if(ports.length == 0) {
    println("ERROR NO PORTS AVAILABLE");
    exit();
  }
  
  // ask user to select serial port
  String preset="";
  String port = (String)JOptionPane.showInputDialog(frame, "Select serial port", "Cap touch", JOptionPane.PLAIN_MESSAGE, null, ports, null);
  
  if(port != null && port.length() > 0) {
    arduinoPort = new Serial(this, port, 9600);
    arduinoPort.bufferUntil('\n');
  } else {
    print("Cancelled");
    exit();
  }
  
  buttons = new CapButton[NUM_BUTTONS];
  int gutter = 0;
  int totalWidth = NUM_BUTTONS * BUTTON_WIDTH + (NUM_BUTTONS-1)*gutter;
  float x0 = (width - totalWidth)/2f;
  float y0 = (height - BUTTON_HEIGHT)/2f;
  for(int i=0; i<NUM_BUTTONS; ++i) {
    buttons[i] = new CapButton(x0 + i*(BUTTON_WIDTH+gutter), y0, BUTTON_WIDTH, BUTTON_HEIGHT);
  }
  
  noLoop();
}

// SHOW PRESS KEY WHEN READY
// SHOW 3 2 1
// trials
// SHOW END

void draw() {
  background(255);
  for(int i=0; i<buttons.length; ++i) {
    buttons[i].display();
  }

  updateSliderPos();  
  if(show) {
    ellipseMode(CENTER);
    fill(0);
    ellipse(sliderPos, 640, 25, 25);
  }
}

void keyPressed() {
  if(key == 'r') {
    setBaseline();
  } 
}

void setBaseline() {
  arduinoPort.write(49);
  arduinoPort.write(" ");
}

void updateSliderPos() {
  int sum = 0;
  int maxIndex = 0, maxValue = -1;
  for(int i=0; i<NUM_BUTTONS; ++i) {
    int cap = buttons[i].getCapacitance();
    if(maxValue < 0 || cap > maxValue) {
      maxIndex = i;
      maxValue = cap;
    }
    
    sum += cap;
  }
  
  show = sum > 100;
  
  int adjIndex, adjValue;
  if(maxIndex == 0) {
    adjIndex = 1;
  } else if(maxIndex == NUM_BUTTONS - 1) {
    adjIndex = NUM_BUTTONS - 2;
  } else if(buttons[maxIndex - 1].getCapacitance() > buttons[maxIndex + 1].getCapacitance()) {
    adjIndex = maxIndex - 1;
  } else {
    adjIndex = maxIndex + 1;
  }
  
  adjValue = buttons[adjIndex].getCapacitance();
  
  if(maxValue + adjValue == 0) {
    sliderPos = buttons[maxIndex].getX();
  } else {
    sliderPos = (buttons[maxIndex].getX() * maxValue + buttons[adjIndex].getX() * adjValue) / (maxValue + adjValue); 
  }
}

void serialEvent(Serial p) {
  String line = p.readStringUntil('\n').trim();
  //println(line);
  String[] toks = line.split(",");
 
  if(toks.length > 9) {
    println(line);
    
    for(int i=1; i<=NUM_BUTTONS; ++i) {
      println(toks[i]);
      toks[i] = toks[i].trim(); //Trimming space in front so it does not crash
      int c = Integer.parseInt(toks[i]);
      buttons[i-1].setCapacitance(c);
    }
    
   
    redraw();
    
    
  }
  
}
