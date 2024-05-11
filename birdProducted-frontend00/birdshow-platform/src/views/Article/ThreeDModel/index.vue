<script setup lang="ts">
  import { useRoute } from 'vue-router';
  import * as THREE from 'three';
  import { onMounted } from "vue";
  import { GLTFLoader } from "three/examples/jsm/loaders/GLTFLoader";
  import { OrbitControls } from "three/examples/jsm/controls/OrbitControls";
  const route = useRoute();
  const id = route.params.id;
  


  let camera: THREE.PerspectiveCamera;
  let renderer: THREE.WebGLRenderer;
  let scene: THREE.Scene;

  function initThree() {
    scene = new THREE.Scene();
    scene.background = new THREE.Color("#eee");

    const threeDemo = document.getElementById("three");
    renderer = new THREE.WebGLRenderer({ canvas: threeDemo, antialias: true });
    camera = new THREE.PerspectiveCamera(30, window.innerWidth / window.innerHeight, 0.1, 1000);
    camera.position.z = 10;

    const controls = new OrbitControls(camera, renderer.domElement);
    controls.update();

    const animate = () => {
      if (renderer && camera) {
        renderer.render(scene, camera);
        requestAnimationFrame(animate);

        if (resizeDevicePixel(renderer)) {
          const canvas = renderer.domElement;
          camera.aspect = canvas.clientWidth / canvas.clientHeight;
          camera.updateProjectionMatrix();
        }
      }
    };
    animate();
  }

  onMounted(() => {
    initThree();

    const gltfLoader = new GLTFLoader();
    gltfLoader.load("http://127.0.0.1:9005/test/article/threeD/bird-orange/scene.gltf", (gltf) => {
      const model = gltf.scene;
      model.traverse((obj) => {
        let imgTexture = new THREE.TextureLoader().load("http://127.0.0.1:9005/test/article/threeD/bird-orange/textures/BirdOrange_LMB_baseColor.png");
        //调整纹理图的方向
        imgTexture.flipY = false;
        const material = new THREE.MeshBasicMaterial({ map: imgTexture });
        if (obj.isMesh) {
          obj.material = material;
          obj.castShadow = true;
          obj.receiveShadow = true;
        }
      });
      scene.add(model);
    });
  });

  function resizeDevicePixel(renderer: THREE.WebGLRenderer) {
    const canvas = renderer.domElement;
    let width = window.innerWidth;
    let height = window.innerHeight;
    let devicePixelWidth = canvas.width / window.devicePixelRatio;
    let devicePixelHeight = canvas.height / window.devicePixelRatio;
    const needResize = devicePixelWidth !== width || devicePixelHeight !== height;
    if (needResize) {
      renderer.setSize(width, height, false);
    }
    return needResize;
  }
</script>



<template>
  <div>
    <p>{{ id }}</p>
  </div>
  <div>
    <canvas id="three"></canvas>
  </div>
</template>
  
<style scoped lang="scss">
#three {
  width: 100vw;
  height: 100vh;
  position: absolute;
  top: 0;
  left: 0;
}
</style>