// SPDX-License-Identifier: MIT
pragma solidity 0.8.4;
pragma abicoder v2;

contract Dare {

    struct Bounty {
        address payable from;
        uint bounty;
    }

    // Check if challenge exists
    mapping (string => mapping (string => bool)) hasChallenge; 

    // List all the challenges available for the stream
    mapping (string => string[]) public challenges;

    // List all the bounties available for each challenge
    mapping (string => mapping(string => Bounty[])) public bounties;

    // How much total value of bounty per challenge
    mapping (string => mapping(string => uint)) challengeAmount;


    function setChallenge(string memory _streamId, string memory _challenge) external payable {
        require (msg.value > 0, "Cannot set challenge without a value");
        
        if (!hasChallenge[_streamId][_challenge]) {
            // Challenge doesn't exist. Create new challenge.
            challenges[_streamId].push(_challenge);                        
            hasChallenge[_streamId][_challenge] = true;
        } 

        bounties[_streamId][_challenge].push(
            Bounty(payable(msg.sender), msg.value)
        );
        
        challengeAmount[_streamId][_challenge] += msg.value;
    }

    function getChallenges(string memory _streamId) external view returns(string[] memory) {
        return challenges[_streamId];
    }

    function getTotalBounty(string memory _streamId, string memory _challenge) external view returns (uint) {
        return challengeAmount[_streamId][_challenge];
    }

    function returnBounties(string memory _streamId) external {
        for (uint i = 0; i < challenges[_streamId].length; i++) {
            string memory challenge = challenges[_streamId][i];
            Bounty[] memory bountyList = bounties[_streamId][challenge];
            for (uint j = 0; j < bountyList.length; j++) {
                bountyList[i].from.transfer(bountyList[j].bounty);
            }
        }
    }     
}
