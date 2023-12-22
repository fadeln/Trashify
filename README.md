<p align="center"> 
  <img width="250" alt="logo_trashify (1)" src="https://github.com/hefrilia/Trashify/assets/92602362/2ce54f84-97a1-4270-9ba4-218e8dfc7b13">
</p>

# 📱 About Trashify 
<p align="center"> 
  <img width="500" alt="trashify" src="https://github.com/hefrilia/Trashify/assets/92602362/4bea2bd5-218e-4b0d-8224-189de8971e27">
</p

"Trashify" is a transformative project aimed at addressing Indonesia's pressing environmental issue of improper waste disposal, which often results in devastating natural disasters such as flooding. Our mission is to empower communities to take control of this problem by turning trash into valuable resources. <br>
Problem Statement: Indonesia faces a grave environmental challenge as improper waste disposal leads to frequent natural disasters, endangering lives and ecosystems. The need for responsible waste management is more critical than ever. <br>
We already building a Cloud architecture model for Machine Learning and the Application, and the result was satisfactory, yet there is a thing we still trying to construct in our next timeline, which is not our main feature. <br>
<br>

# 📃 How to Install this Project

## ▶️ How to Clone and Use This Project
1. Clone or download Source Code
   - In Your Terminal or Andorid Studio, clone repository `git clone https://github.com/hefrilia/Trashify.git`
   - Otherwise use Git, you can **Download Zip** and _extract_ to your own directory (example: C:\Users\Username\AndroidStudioProjects)
2. Wait for the Build Gradle to complete
3. Congrats, you are done to Install this project
4. Run Apps With Your Emulator or Android Phone

## ▶️ How to Install and Use Apps
1. Download this Application in this link: https://drive.google.com/drive/folders/1AzjJ6J2rg56jn2-MdO1jtWtsp2ex2Dfn?usp=sharing
2. Install Apps in Your Android Phone
3. Use This Application
<br>

# 📲 Application Features and Appearance
## ☑️ Main Features and Completed
1. **Splash Screen**
   <p align="center"><img width="250" alt="trashify" src="https://github.com/hefrilia/Trashify/assets/92602362/8866684c-583e-4f9a-8ca8-6759df5ed7af"></p><br>
   
2. **Welcome Page**
   <p align="center"><img width="250" alt="trashify" src="https://github.com/hefrilia/Trashify/assets/92602362/b49fc555-adca-4aa9-a4fc-a79702a51028"></p><br>
   
3. **Login and Register**
   <p align="center">
     <img width="250" alt="trashify" src="https://github.com/hefrilia/Trashify/assets/92602362/68012cb0-1ff4-4003-b176-cf2b07475bdd">  
     <img width="250" alt="trashify" src="https://github.com/hefrilia/Trashify/assets/92602362/9e344284-8ac0-441e-9428-da391ae03314">
   </p><br>

4. **Home Page**
   <p align="center"><img width="250" alt="trashify" src="https://github.com/hefrilia/Trashify/assets/92602362/7d4f87f0-30eb-4d67-b043-3dbb95a2217d"></p><br>

5. **Scan Page**
   <p align="center"><img width="250" alt="trashify" src="https://github.com/hefrilia/Trashify/assets/92602362/4fc28131-0c3a-479e-b575-488ec80a36e0"></p><br>

6. **Scan Result**
   <p align="center"><img width="250" alt="trashify" src="https://github.com/hefrilia/Trashify/assets/92602362/701b5faf-1054-47ea-ba99-12100bf20536"></p><br>

7. **Crafting Tutorial**
   <p align="center"><img width="250" alt="trashify" src="https://github.com/hefrilia/Trashify/assets/92602362/7b1db9c5-b8ac-42ba-b413-1d437aa90ef4"></p><br>

8. **Post Page**
   <p align="center"><img width="250" alt="trashify" src="https://github.com/hefrilia/Trashify/assets/92602362/f4f6b079-4700-4282-a555-a7d590f62241"></p><br>

9. **Detail Post**
   <p align="center"><img width="250" alt="trashify" src="https://github.com/hefrilia/Trashify/assets/92602362/1b34e323-be47-4cb5-89de-257a63c6920e"></p><br>

10. **Add/Upload Post**
    <p align="center"><img width="250" alt="trashify" src="https://github.com/hefrilia/Trashify/assets/92602362/85dc5a48-cf45-4347-a9e4-07bee21e3160"></p><br>

## 🔜 Under Construction Development
1. **Leaderboard** <br>
   This feature is used to rank how many users like posts that users post. Users who are in the top 5 of these leaderboards, later will get badges or points that can be exchanged for prizes. The purpose of this feature is to provide encouragement with rewards to users to produce works or creations from waste so as to minimize the current waste. <br>
   <p align="center"><img width="250" alt="trashify" src="https://github.com/hefrilia/Trashify/assets/92602362/9fb7f06d-262b-473c-8a73-20131104475f"></p><br>
   
2. **Community** <br>
   This feature is used to provide information to users when there are volunteer community events that focus on environmental initiatives, such as joint efforts to clean the ocean or plant trees. <br>
   <p align="center"><img width="250" alt="trashify" src="https://github.com/hefrilia/Trashify/assets/92602362/620ee92e-11d5-404f-880c-e72b9c486b9e"></p><br>



# Machine Learning Trash Classification

## Data Collection

We collected data from various sources to ensure diversity in the dataset. The following datasets were used:

- [Waste Pictures Kaggle Dataset](https://www.kaggle.com/datasets/wangziang/waste-pictures)
- [TrashNet Dataset on Hugging Face](https://huggingface.co/datasets/garythung/trashnet)
- Google Images using the [Download all Images Extension](https://chrome.google.com/webstore/detail/download-all-images/ifipmflagepipjokmbdecpmjbibjnakm)

## Classify : 

1. Cardboard
2. Glass 
3. Metal
4. Paper
5. Plastic
6. Organic

## Preprocessing Data

The collected data is preprocessed to enhance the model's performance. We classify materials into six classes and apply data augmentation techniques to minimize overfitting.

## Model Creation

We employ transfer learning using TensorFlow Hub's MobileNet V3 to leverage pre-trained weights and achieve high accuracy. The model is fine-tuned by adding additional dense layers, dropout layers for regularization, and a softmax layer for classification.

## Model Conversion

To make the model compatible with Android devices, we convert it into TensorFlow Lite format. This enables the model to be deployed efficiently on mobile devices while maintaining its classification capabilities.


