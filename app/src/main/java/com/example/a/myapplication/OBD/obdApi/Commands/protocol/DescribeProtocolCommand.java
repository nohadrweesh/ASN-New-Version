package com.example.a.myapplication.OBD.obdApi.Commands.protocol;

/**
 * Created by Ahmed on 3/27/2018.
 */

import com.example.a.myapplication.OBD.obdApi.enums.AvailableCommandNames;

/**

 * Describe the current Protocol.
 * If a protocol is chosen and the automatic option is
 * also selected, AT DP will show the word 'AUTO' before
 * the protocol description. Note that the description
 * shows the actual protocol names, not the numbers
 * used by the protocol setting commands.
 *
 * @since 1.0-RC12
 */
public class DescribeProtocolCommand extends ObdProtocolCommand {

    /**
     * <p>Constructor for DescribeProtocolCommand.</p>
     */
    public DescribeProtocolCommand() {
        super("AT DP");
    }

    /** {@inheritDoc} */
    @Override
    public String getFormattedResult() {
        return getResult();
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return AvailableCommandNames.DESCRIBE_PROTOCOL.getValue();
    }

}
