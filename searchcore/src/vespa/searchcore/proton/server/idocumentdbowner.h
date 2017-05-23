// Copyright 2016 Yahoo Inc. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.

#pragma once

#include <memory>
#include <cstdint>

namespace proton {

class IDocumentDBReferenceRegistry;

class IDocumentDBOwner
{
public:
    virtual ~IDocumentDBOwner();

    virtual bool isInitializing() const = 0;
    virtual uint32_t getDistributionKey() const = 0;
    virtual std::shared_ptr<IDocumentDBReferenceRegistry> getDocumentDBReferenceRegistry() const = 0;
};

} // namespace proton
