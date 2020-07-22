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
package com.xcash.models;


import java.io.Serializable;

public class Delegate implements Serializable {

    private String total_vote_count;
    private String delegate_name;
    private String shared_delegate_status;
    private String delegate_fee;
    private String block_verifier_score;
    private String online_status;
    private String block_verifier_total_rounds;
    private String block_verifier_online_percentage;

    public String getTotal_vote_count() {
        return total_vote_count;
    }

    public void setTotal_vote_count(String total_vote_count) {
        this.total_vote_count = total_vote_count;
    }

    public String getDelegate_name() {
        return delegate_name;
    }

    public void setDelegate_name(String delegate_name) {
        this.delegate_name = delegate_name;
    }

    public String getShared_delegate_status() {
        return shared_delegate_status;
    }

    public void setShared_delegate_status(String shared_delegate_status) {
        this.shared_delegate_status = shared_delegate_status;
    }

    public String getDelegate_fee() {
        return delegate_fee;
    }

    public void setDelegate_fee(String delegate_fee) {
        this.delegate_fee = delegate_fee;
    }

    public String getBlock_verifier_score() {
        return block_verifier_score;
    }

    public void setBlock_verifier_score(String block_verifier_score) {
        this.block_verifier_score = block_verifier_score;
    }

    public String getOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }

    public String getBlock_verifier_total_rounds() {
        return block_verifier_total_rounds;
    }

    public void setBlock_verifier_total_rounds(String block_verifier_total_rounds) {
        this.block_verifier_total_rounds = block_verifier_total_rounds;
    }

    public String getBlock_verifier_online_percentage() {
        return block_verifier_online_percentage;
    }

    public void setBlock_verifier_online_percentage(String block_verifier_online_percentage) {
        this.block_verifier_online_percentage = block_verifier_online_percentage;
    }

    @Override
    public String toString() {
        return "Delegate{" +
                "total_vote_count='" + total_vote_count + '\'' +
                ", delegate_name='" + delegate_name + '\'' +
                ", shared_delegate_status='" + shared_delegate_status + '\'' +
                ", delegate_fee='" + delegate_fee + '\'' +
                ", block_verifier_score='" + block_verifier_score + '\'' +
                ", online_status='" + online_status + '\'' +
                ", block_verifier_total_rounds='" + block_verifier_total_rounds + '\'' +
                ", block_verifier_online_percentage='" + block_verifier_online_percentage + '\'' +
                '}';
    }
}
