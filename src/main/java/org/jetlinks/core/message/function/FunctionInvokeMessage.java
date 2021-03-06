package org.jetlinks.core.message.function;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.jetlinks.core.message.CommonDeviceMessage;
import org.jetlinks.core.message.MessageType;
import org.jetlinks.core.message.RepayableDeviceMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouhao
 * @since 1.0.0
 */
@Getter
@Setter
public class FunctionInvokeMessage extends CommonDeviceMessage
        implements RepayableDeviceMessage<FunctionInvokeMessageReply> {

    private String functionId;

    public FunctionInvokeMessage() {

    }

    public MessageType getMessageType() {
        return MessageType.INVOKE_FUNCTION;
    }


    private List<FunctionParameter> inputs = new ArrayList<>();

    public FunctionInvokeMessage addInput(String name, Object value) {
        return this.addInput(new FunctionParameter(name, value));
    }

    public FunctionInvokeMessage addInput(FunctionParameter parameter) {
        inputs.add(parameter);
        return this;
    }

    @Override
    public FunctionInvokeMessage addHeader(String header, Object value) {
        super.addHeader(header, value);
        return this;
    }

    @Override
    public FunctionInvokeMessage removeHeader(String header) {
        super.removeHeader(header);
        return this;
    }

    @Override
    public void fromJson(JSONObject jsonObject) {
        super.fromJson(jsonObject);
        this.functionId = jsonObject.getString("functionId");
    }

    @Override
    public FunctionInvokeMessageReply newReply() {
        return new FunctionInvokeMessageReply().from(this);
    }
}
