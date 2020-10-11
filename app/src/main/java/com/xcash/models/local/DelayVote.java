 /*
  * Copyright (c) 2019-2020 The X-Cash Foundation
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
 package com.xcash.models.local;


 import com.xcash.wallet.aidl.OnNormalListener;

 public class DelayVote {

     private long voteTimestamp;
     private String value;
     private OnNormalListener onNormalListener;

     public long getVoteTimestamp() {
         return voteTimestamp;
     }

     public void setVoteTimestamp(long voteTimestamp) {
         this.voteTimestamp = voteTimestamp;
     }

     public String getValue() {
         return value;
     }

     public void setValue(String value) {
         this.value = value;
     }

     public OnNormalListener getOnNormalListener() {
         return onNormalListener;
     }

     public void setOnNormalListener(OnNormalListener onNormalListener) {
         this.onNormalListener = onNormalListener;
     }
 }
