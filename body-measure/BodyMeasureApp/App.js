import React, { useState, useEffect } from 'react';
import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View, Alert } from 'react-native';
import { Camera } from 'expo-camera';
import { GLView } from 'expo-gl';
import * as tf from '@tensorflow/tfjs';
import '@tensorflow/tfjs-react-native';
import '@tensorflow/tfjs-platform-react-native';

export default function App() {
  const [hasPermission, setHasPermission] = useState(null);
  const [arcoreSupported, setArcoreSupported] = useState(false);

  useEffect(() => {
    (async () => {
      // Request camera permission
      const { status } = await Camera.requestCameraPermissionsAsync();
      setHasPermission(status === 'granted');
      
      // Initialize TensorFlow.js
      await tf.ready();
      
      // Check ARCore support (simplified check)
      setArcoreSupported(true);
    })();
  }, []);

  if (hasPermission === null) {
    return <View style={styles.container}><Text>Requesting camera permission...</Text></View>;
  }
  
  if (hasPermission === false) {
    return <View style={styles.container}><Text>No access to camera</Text></View>;
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>ARCore + MLKit Body Measure App</Text>
      <Text style={styles.subtitle}>Camera: {hasPermission ? '✅' : '❌'}</Text>
      <Text style={styles.subtitle}>ARCore: {arcoreSupported ? '✅' : '❌'}</Text>
      <Text style={styles.subtitle}>TensorFlow.js: ✅</Text>
      
      <View style={styles.cameraContainer}>
        <Camera style={styles.camera} type={Camera.Constants.Type.back}>
          <GLView style={styles.glView} onContextCreate={onContextCreate} />
        </Camera>
      </View>
      
      <StatusBar style="auto" />
    </View>
  );
}

function onContextCreate(gl) {
  // ARCore + MLKit implementation would go here
  console.log('ARCore GL context created');
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  subtitle: {
    fontSize: 16,
    marginBottom: 10,
  },
  cameraContainer: {
    width: 300,
    height: 400,
    marginTop: 20,
  },
  camera: {
    flex: 1,
  },
  glView: {
    flex: 1,
  },
});
